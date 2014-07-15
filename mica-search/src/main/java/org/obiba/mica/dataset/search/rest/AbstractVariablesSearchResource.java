/*
 * Copyright (c) 2014 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.dataset.search.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONException;
import org.obiba.mica.dataset.domain.DatasetVariable;
import org.obiba.mica.search.rest.AbstractSearchResource;
import org.obiba.mica.search.rest.QueryDtoParser;
import org.obiba.mica.web.model.Dtos;
import org.obiba.mica.web.model.MicaSearch;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.obiba.mica.web.model.MicaSearch.QueryDto;
import static org.obiba.mica.web.model.MicaSearch.QueryResultDto;

public abstract class AbstractVariablesSearchResource extends AbstractSearchResource {

  private static final String VARIABLE_FACETS_YML = "variable-facets.yml";

  @Inject
  private Dtos dtos;

  @Inject
  private ObjectMapper objectMapper;

  @GET
  @Timed
  public QueryResultDto list(@QueryParam("from") @DefaultValue("0") int from,
      @QueryParam("size") @DefaultValue("10") int size, @QueryParam("detailed") @DefaultValue("false") boolean detailed)
      throws JSONException, IOException {

    return execute(QueryBuilders.matchAllQuery(), from, size, detailed);
  }

  @POST
  @Timed
  public QueryResultDto list(QueryDto dtoQuery) throws IOException {
    return execute(QueryDtoParser.newParser().parse(dtoQuery), dtoQuery.getFrom(), dtoQuery.getSize(),
        dtoQuery.getDetailed());
  }

  @Override
  protected Resource getAggregationsDescription() {
    return new ClassPathResource(VARIABLE_FACETS_YML);
  }

  @Override
  protected void processHits(QueryResultDto.Builder builder, boolean detailed, SearchHits hits) throws IOException {
    MicaSearch.DatasetVariableResultDto.Builder resBuilder = MicaSearch.DatasetVariableResultDto.newBuilder();
    for(SearchHit hit : hits) {
      resBuilder.addSummaries(dtos.asDto(DatasetVariable.IdResolver.from(hit.getId())));
      if(detailed) {
        InputStream inputStream = new ByteArrayInputStream(hit.getSourceAsString().getBytes());
        resBuilder.addVariables(dtos.asDto(objectMapper.readValue(inputStream, DatasetVariable.class)));
      }
    }
    builder.setExtension(MicaSearch.DatasetVariableResultDto.result, resBuilder.build());
  }
}