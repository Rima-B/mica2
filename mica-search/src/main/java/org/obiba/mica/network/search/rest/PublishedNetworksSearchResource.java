/*
 * Copyright (c) 2017 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.network.search.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.obiba.mica.search.JoinQueryExecutor;
import org.obiba.mica.search.csvexport.GenericReportGenerator;
import org.obiba.mica.search.queries.rql.RQLQueryBuilder;
import org.obiba.mica.spi.search.QueryType;
import org.obiba.mica.spi.search.Searcher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Strings;

import static org.obiba.mica.web.model.MicaSearch.JoinQueryResultDto;

@Path("/networks")
@RequiresAuthentication
@Scope("request")
@Component
public class PublishedNetworksSearchResource {

  public static final String DEFAULT_SORT = "script";

  @Inject
  JoinQueryExecutor joinQueryExecutor;

  @Inject
  private Searcher searcher;

  @Inject
  private GenericReportGenerator genericReportGenerator;

  @GET
  @Timed
  public JoinQueryResultDto rqlList(@QueryParam("from") @DefaultValue("0") int from,
                                    @QueryParam("limit") @DefaultValue("10") int limit, @QueryParam("sort") @DefaultValue("name") String sort,
                                    @QueryParam("order") @DefaultValue("asc") String order, @QueryParam("locale") @DefaultValue("en") String locale)
      throws IOException {

    String rql = RQLQueryBuilder.newInstance().target(
        RQLQueryBuilder.TargetQueryBuilder.networkInstance().exists("id").limit(from, limit).sort(sort, order).build())
        .locale(locale).buildArgsAsString();

    return joinQueryExecutor.query(QueryType.NETWORK, searcher.makeJoinQuery(rql));
  }

  @GET
  @Path("/_rql")
  @Timed
  public JoinQueryResultDto rqlQuery(@QueryParam("query") String query) throws IOException {
    String queryStr = query;
    if (Strings.isNullOrEmpty(queryStr)) queryStr = "network(exists(Mica_network.id))";
    return joinQueryExecutor.query(QueryType.NETWORK, searcher.makeJoinQuery(queryStr));
  }

  @GET
  @Path("/_rql_csv")
  @Timed
  public Response rqlQueryAsCsv(@QueryParam("query") String query, @QueryParam("columnsToHide") List<String> columnsToHide) throws IOException {
    StreamingOutput stream = os -> genericReportGenerator.generateCsv(QueryType.NETWORK, query, columnsToHide, os);
    return Response.ok(stream).header("Content-Disposition", "attachment; filename=\"SearchNetworks.csv\"").build();
  }
}
