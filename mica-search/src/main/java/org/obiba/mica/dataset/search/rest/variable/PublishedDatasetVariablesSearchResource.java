/*
 * Copyright (c) 2014 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.dataset.search.rest.variable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.obiba.mica.core.domain.AttributeKey;
import org.obiba.mica.dataset.search.VariableIndexer;
import org.obiba.mica.micaConfig.OpalService;
import org.obiba.mica.search.JoinQueryExecutor;
import org.obiba.mica.search.rest.QueryDtoHelper;
import org.obiba.mica.web.model.Dtos;
import org.obiba.mica.web.model.MicaSearch;
import org.obiba.opal.core.domain.taxonomy.Taxonomy;
import org.obiba.opal.core.domain.taxonomy.Term;
import org.obiba.opal.core.domain.taxonomy.Vocabulary;
import org.springframework.context.annotation.Scope;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Search for variables in the published variable index.
 */
@Path("/variables")
@RequiresAuthentication
@Scope("request")
public class PublishedDatasetVariablesSearchResource {

  @Inject
  private OpalService opalService;

  @Inject
  private JoinQueryExecutor joinQueryExecutor;

  @Inject
  private Dtos dtos;

  @GET
  @Timed
  public MicaSearch.JoinQueryResultDto query(@QueryParam("from") @DefaultValue("0") int from,
    @QueryParam("limit") @DefaultValue("10") int limit, @QueryParam("sort") String sort,
    @QueryParam("order") String order, @QueryParam("query") String query,
    @QueryParam("locale") @DefaultValue("en") String locale) throws IOException {

    return joinQueryExecutor.query(JoinQueryExecutor.QueryType.VARIABLE, MicaSearch.JoinQueryDto.newBuilder()
      .setVariableQueryDto(QueryDtoHelper
        .createQueryDto(from, limit, sort, order, query, locale, Stream.of(VariableIndexer.ANALYZED_FIELDS))).build());
  }

  @POST
  @Timed
  @Path("_search")
  public MicaSearch.JoinQueryResultDto list(MicaSearch.JoinQueryDto joinQueryDto) throws IOException {
    return joinQueryExecutor.query(JoinQueryExecutor.QueryType.VARIABLE, joinQueryDto);
  }

  /**
   * Get the frequency of each taxonomy terms, based on variables aggregation results.
   *
   * @param taxonomyNames
   * @param joinQueryDto
   * @return
   * @throws IOException
   */
  @POST
  @Timed
  @Path("_coverage")
  public MicaSearch.TaxonomiesCoverageDto coverage(@QueryParam("taxonomy") List<String> taxonomyNames,
    MicaSearch.JoinQueryDto joinQueryDto) throws IOException {

    MicaSearch.JoinQueryResultDto result = joinQueryExecutor
      .queryAggregations(JoinQueryExecutor.QueryType.VARIABLE, joinQueryDto);
    List<MicaSearch.AggregationResultDto> aggregations = result.getVariableResultDto().getAggsList();

    Map<String, List<BucketResult>> bucketResultsByTaxonomy = extractBucketResults(joinQueryDto, aggregations).stream()
      .collect(Collectors.groupingBy(BucketResult::getTaxonomy));

    Map<String, Map<String, Integer>> aggsMap = Maps.newHashMap();
    aggregations.forEach(agg -> {
      String name = agg.getAggregation();
      List<MicaSearch.TermsAggregationResultDto> results = agg.getExtension(MicaSearch.TermsAggregationResultDto.terms);
      if(results != null && !results.isEmpty() && name.startsWith("attributes-") && name.endsWith("-und")) {
        String key = name.replaceAll("^attributes-", "").replaceAll("-und$", "");
        if(!aggsMap.containsKey(key)) aggsMap.put(key, Maps.newHashMap());
        results.forEach(res -> aggsMap.get(key).put(res.getKey(), res.getCount()));
      }
    });

    List<MicaSearch.TaxonomyCoverageDto> coverages = Lists.newArrayList();
    getTaxonomies().stream().filter(
      taxonomy -> taxonomyNames == null || taxonomyNames.isEmpty() || taxonomyNames.contains(taxonomy.getName()))
      .forEach(
        taxonomy -> addTaxonomyCoverage(coverages, taxonomy, aggsMap, bucketResultsByTaxonomy.get(taxonomy.getName())));

    MicaSearch.TaxonomiesCoverageDto.Builder builder = MicaSearch.TaxonomiesCoverageDto.newBuilder()//
      .setTotalCount(result.getVariableResultDto().getTotalCount()) //
      .setTotalHits(result.getVariableResultDto().getTotalHits()) //
      .addAllTaxonomies(coverages);

    return builder.build();
  }

  /**
   * Extract the hits per taxonomy term and bucket.
   *
   * @param joinQueryDto
   * @param aggregations
   * @return
   */
  @NotNull
  private List<BucketResult> extractBucketResults(MicaSearch.JoinQueryDto joinQueryDto,
    List<MicaSearch.AggregationResultDto> aggregations) {
    if(joinQueryDto == null || !joinQueryDto.hasVariableQueryDto() ||
      joinQueryDto.getVariableQueryDto().getAggsByCount() == 0) return Collections.emptyList();

    List<String> aggsBy = joinQueryDto.getVariableQueryDto().getAggsByList();

    List<BucketResult> termResults = Lists.newArrayList();

    aggregations.stream().filter(agg -> aggsBy.contains(agg.getAggregation())).forEach(bucket -> {
      String bucketField = bucket.getAggregation(); // studyIds for instance
      bucket.getExtension(MicaSearch.TermsAggregationResultDto.terms).stream() //
        .filter(agg -> agg.getAggsCount() > 0) //
        .forEach(agg -> agg.getAggsList().stream() //
          .filter(t -> !t.getExtension(MicaSearch.TermsAggregationResultDto.terms).isEmpty()) //
          .forEach(t -> termResults.addAll(BucketResult.list(bucketField, agg.getKey(), t))));
    });

    return termResults;
  }

  /**
   * for a {@link org.obiba.opal.core.domain.taxonomy.Taxonomy}, report the number of hits and optionally the
   * number of hits for each bucket.
   *
   * @param coverages
   * @param taxonomy
   * @param aggsMap
   * @param bucketResults
   */
  private void addTaxonomyCoverage(List<MicaSearch.TaxonomyCoverageDto> coverages, Taxonomy taxonomy,
    Map<String, Map<String, Integer>> aggsMap, @Nullable List<BucketResult> bucketResults) {
    if(taxonomy.hasVocabularies()) {
      MicaSearch.TaxonomyCoverageDto.Builder taxoBuilder = MicaSearch.TaxonomyCoverageDto.newBuilder();
      taxoBuilder.setTaxonomy(dtos.asDto(taxonomy));
      List<Integer> hits = Lists.newArrayList();
      String namespace = taxonomy.getName().equals("Default") ? null : taxonomy.getName();
      Map<String, List<BucketResult>> bucketResultsByVocabulary = bucketResults == null
        ? Maps.newHashMap()
        : bucketResults.stream().collect(Collectors.groupingBy(BucketResult::getVocabulary));

      taxonomy.getVocabularies().forEach(vocabulary -> hits.add(addVocabularyCoverage(taxoBuilder, vocabulary,
        aggsMap.get(AttributeKey.getMapKey(vocabulary.getName(), namespace)),
        bucketResults == null ? null : bucketResultsByVocabulary.get(vocabulary.getName()))));
      taxoBuilder.setHits(hits.isEmpty() ? 0 : hits.stream().mapToInt(x -> x).sum());
      // compute the sum of the hits for all vocabularies per bucket
      if(bucketResults != null) {
        Map<String, List<BucketResult>> bucketResultsByBucketField = bucketResults.stream()
          .collect(Collectors.groupingBy(BucketResult::getBucketField));

        bucketResultsByBucketField.keySet().forEach(field -> bucketResultsByBucketField.get(field).stream()
          .collect(Collectors.groupingBy(BucketResult::getBucketValue)).forEach((value, buckets) -> {
            int sumOfHits = buckets.stream().mapToInt(BucketResult::getHits).sum();
            if(sumOfHits > 0) {
              taxoBuilder.addBuckets(
                MicaSearch.BucketCoverageDto.newBuilder().setField(field).setValue(value).setHits(sumOfHits));
            }
          }));
      }
      coverages.add(taxoBuilder.build());
    }
  }

  /**
   * For a taxonomy {@link org.obiba.opal.core.domain.taxonomy.Vocabulary}, report the number of hits and optionally the
   * number of hits for each bucket.
   *
   * @param taxoBuilder
   * @param vocabulary
   * @param hits
   * @param bucketResults
   * @return
   */
  private int addVocabularyCoverage(MicaSearch.TaxonomyCoverageDto.Builder taxoBuilder, Vocabulary vocabulary,
    Map<String, Integer> hits, @Nullable List<BucketResult> bucketResults) {
    int sumOfHits = 0;
    if(vocabulary.hasTerms()) {
      Map<String, List<BucketResult>> bucketResultsByTerm = bucketResults == null
        ? Maps.newHashMap()
        : bucketResults.stream().collect(Collectors.groupingBy(BucketResult::getTerm));

      MicaSearch.VocabularyCoverageDto.Builder vocBuilder = MicaSearch.VocabularyCoverageDto.newBuilder();
      vocBuilder.setVocabulary(dtos.asDto(vocabulary));
      vocabulary.getTerms()
        .forEach(term -> addTermCoverage(vocBuilder, term, hits, bucketResultsByTerm.get(term.getName())));
      // only one term can be applied at a time, then the sum of the term hits is the number of variables
      // that cover this vocabulary
      sumOfHits = hits == null ? 0 : hits.values().stream().mapToInt(x -> x).sum();
      vocBuilder.setHits(sumOfHits);
      if(!vocabulary.isRepeatable()) {
        vocBuilder.setCount(sumOfHits);
      }
      // compute the sum of the hits for all terms per bucket
      if(bucketResults != null) {
        Map<String, List<BucketResult>> bucketResultsByBucketField = bucketResults.stream()
          .collect(Collectors.groupingBy(BucketResult::getBucketField));

        bucketResultsByBucketField.keySet().forEach(field -> bucketResultsByBucketField.get(field).stream()
          .collect(Collectors.groupingBy(BucketResult::getBucketValue)).forEach((value, buckets) -> {
            int sumOfBucketHits = buckets.stream().mapToInt(BucketResult::getHits).sum();
            if (sumOfBucketHits > 0) {
              MicaSearch.BucketCoverageDto.Builder builder = MicaSearch.BucketCoverageDto.newBuilder().setField(field)
                .setValue(value).setHits(sumOfBucketHits);
              if(!vocabulary.isRepeatable()) builder.setCount(builder.getHits());
              vocBuilder.addBuckets(builder);
            }
          }));
      }
      taxoBuilder.addVocabularies(vocBuilder);
    }
    return sumOfHits;
  }

  /**
   * For a taxonomy {@link org.obiba.opal.core.domain.taxonomy.Term}, report the number of hits and optionally
   * the number of hits for each bucket.
   *
   * @param vocBuilder
   * @param term
   * @param hits
   * @param bucketResults
   */
  private void addTermCoverage(MicaSearch.VocabularyCoverageDto.Builder vocBuilder, Term term,
    Map<String, Integer> hits, @Nullable List<BucketResult> bucketResults) {
    MicaSearch.TermCoverageDto.Builder termBuilder = MicaSearch.TermCoverageDto.newBuilder();
    termBuilder.setTerm(dtos.asDto(term));
    termBuilder.setHits(0);
    // add the hits per buckets
    if(bucketResults != null) {
      termBuilder.addAllBuckets(bucketResults.stream().map(
        b -> MicaSearch.BucketCoverageDto.newBuilder().setField(b.getBucketField()).setValue(b.getBucketValue())
          .setHits(b.getHits()).build()).collect(Collectors.toList()));
    }
    if(hits != null && hits.containsKey(term.getName())) termBuilder.setHits(hits.get(term.getName()));
    vocBuilder.addTerms(termBuilder);
  }

  @NotNull
  private List<Taxonomy> getTaxonomies() {
    List<Taxonomy> taxonomies = null;
    try {
      taxonomies = opalService.getTaxonomies();
    } catch(Exception e) {
      // ignore
    }
    return taxonomies == null ? Collections.emptyList() : taxonomies;
  }

  /**
   * The number of variable hits per bucket and taxonomy term.
   */
  private static class BucketResult {

    private final String bucketField;

    private final String bucketValue;

    private final String taxonomy;

    private final String vocabulary;

    private final String term;

    private final int hits;

    private BucketResult(@Nullable String bucketField, @Nullable String bucketValue, String taxonomy, String vocabulary,
      String term, int hits) {
      this.bucketField = bucketField;
      this.bucketValue = bucketValue;
      this.taxonomy = taxonomy;
      this.vocabulary = vocabulary;
      this.term = term;
      this.hits = hits;
    }

    private static List<BucketResult> list(@Nullable String bucketField, @Nullable String bucketValue,
      MicaSearch.AggregationResultDto agg) {
      String key = agg.getAggregation().replaceAll("^attributes-", "").replaceAll("-und$", "");
      AttributeKey attrKey = AttributeKey.from(key);
      String taxonomy = attrKey.hasNamespace(null) ? "Default" : attrKey.getNamespace();
      String vocabulary = attrKey.getName();
      return agg.getExtension(MicaSearch.TermsAggregationResultDto.terms).stream()
        .map(t -> new BucketResult(bucketField, bucketValue, taxonomy, vocabulary, t.getKey(), t.getCount()))
        .collect(Collectors.toList());
    }

    public String getBucketField() {
      return bucketField;
    }

    public String getBucketValue() {
      return bucketValue;
    }

    public String getTaxonomy() {
      return taxonomy;
    }

    public String getVocabulary() {
      return vocabulary;
    }

    public String getTerm() {
      return term;
    }

    public int getHits() {
      return hits;
    }

    public String toString() {
      return "[" + bucketField + "," + bucketValue + "," + taxonomy + "," + vocabulary + "," + term + "]=" + hits;
    }
  }
}
