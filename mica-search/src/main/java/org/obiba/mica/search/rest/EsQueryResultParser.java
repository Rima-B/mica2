/*
 * Copyright (c) 2014 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.search.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.obiba.mica.web.model.MicaSearch;

import static org.obiba.mica.web.model.MicaSearch.AggregationResultDto;
import static org.obiba.mica.web.model.MicaSearch.StatsAggregationResultDto;
import static org.obiba.mica.web.model.MicaSearch.TermsAggregationResultDto;

public class EsQueryResultParser {

  private long totalCount;

  private EsQueryResultParser() {}

  public static EsQueryResultParser newParser() {
    return new EsQueryResultParser();
  }

  public List<AggregationResultDto> parseAggregations(@NotNull Aggregations aggregations) {
    List<AggregationResultDto> aggResults = new ArrayList();
    aggregations.forEach(aggregation -> {

      MicaSearch.AggregationResultDto.Builder aggResultBuilder = MicaSearch.AggregationResultDto.newBuilder();
      aggResultBuilder.setAggregation(aggregation.getName());
      String aggType = ((InternalAggregation) aggregation).type().name();

      switch (aggType) {
        case "stats":
          Stats stats = (Stats) aggregation;
          if (stats.getCount() > 0) {
            aggResultBuilder.setExtension(StatsAggregationResultDto.stats,
                StatsAggregationResultDto.newBuilder().setCount(stats.getCount()).setMin(stats.getMin())
                    .setMax(stats.getMax()).setAvg(stats.getAvg()).setSum(stats.getSum()).build());
          }
          break;
        case "terms":
          ((Terms) aggregation).getBuckets().forEach(
              bucket -> aggResultBuilder.addExtension(TermsAggregationResultDto.terms,
                  TermsAggregationResultDto.newBuilder().setKey(bucket.getKey()).setCount((int) bucket.getDocCount())
                      .build()));
          break;
        case "global":
          totalCount = ((Global)aggregation).getDocCount();
          // do not include in the list of aggregations
          return;

        default:
          throw new RuntimeException("Unsupported aggregation type " + aggType);
      }

      aggResults.add(aggResultBuilder.build());

    });

    return aggResults;
  }

  public long getTotalCount() {
    return totalCount;
  }

}