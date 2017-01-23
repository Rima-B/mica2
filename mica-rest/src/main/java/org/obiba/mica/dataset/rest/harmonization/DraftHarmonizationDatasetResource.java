/*
 * Copyright (c) 2017 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.dataset.rest.harmonization;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableList;
import org.obiba.mica.AbstractGitPersistableResource;
import org.obiba.mica.core.domain.OpalTable;
import org.obiba.mica.core.domain.PublishCascadingScope;
import org.obiba.mica.core.domain.RevisionStatus;
import org.obiba.mica.core.service.AbstractGitPersistableService;
import org.obiba.mica.dataset.domain.Dataset;
import org.obiba.mica.dataset.domain.HarmonizationDataset;
import org.obiba.mica.dataset.domain.HarmonizationDatasetState;
import org.obiba.mica.dataset.service.HarmonizationDatasetService;
import org.obiba.mica.security.rest.SubjectAclResource;
import org.obiba.mica.web.model.Dtos;
import org.obiba.mica.web.model.Mica;
import org.obiba.opal.web.model.Magma;
import org.obiba.opal.web.model.Search;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Scope("request")
public class DraftHarmonizationDatasetResource extends
  AbstractGitPersistableResource<HarmonizationDatasetState,HarmonizationDataset> {

  @Inject
  private ApplicationContext applicationContext;

  @Inject
  private HarmonizationDatasetService datasetService;

  @Inject
  private Dtos dtos;

  private String id;

  public void setId(String id) {
    this.id = id;
  }

  @GET
  public Mica.DatasetDto get(@QueryParam("key") String key) {
    checkPermission("/draft/harmonization-dataset", "VIEW", key);
    return dtos.asDto(getDataset(), true);
  }

  @GET
  @Path("/model")
  @Produces("application/json")
  public Map<String, Object> getModel() {
    checkPermission("/draft/harmonization-dataset", "VIEW");
    return getDataset().getModel();
  }

  @DELETE
  public void delete(){
    checkPermission("/draft/harmonization-dataset", "DELETE");
    datasetService.delete(id);
  }

  @PUT
  @Timed
  public Response update(Mica.DatasetDto datasetDto, @Context UriInfo uriInfo) {
    checkPermission("/draft/harmonization-dataset", "EDIT");
    if(!datasetDto.hasId() || !datasetDto.getId().equals(id))
      throw new IllegalArgumentException("Not the expected dataset id");
    Dataset dataset = dtos.fromDto(datasetDto);
    if(!(dataset instanceof HarmonizationDataset)) throw new IllegalArgumentException("An harmonization dataset is expected");

    datasetService.save((HarmonizationDataset) dataset);
    return Response.noContent().build();
  }

  @PUT
  @Path("/_index")
  @Timed
  public Response index() {
    checkPermission("/draft/harmonization-dataset", "EDIT");
    datasetService.index(id);
    return Response.noContent().build();
  }

  @PUT
  @Path("/_publish")
  public Response publish(@QueryParam("cascading") @DefaultValue("UNDER_REVIEW") String cascadingScope) {
    checkPermission("/draft/harmonization-dataset", "PUBLISH");
    datasetService.publish(id, true, PublishCascadingScope.valueOf(cascadingScope.toUpperCase()));
    return Response.noContent().build();
  }

  @DELETE
  @Path("/_publish")
  public Response unPublish() {
    checkPermission("/draft/harmonization-dataset", "PUBLISH");
    datasetService.publish(id, false);
    return Response.noContent().build();
  }

  @GET
  @Path("/table")
  public Magma.TableDto getTable() {
    checkPermission("/draft/harmonization-dataset", "VIEW");
    return datasetService.getTableDto(getDataset());
  }

  @GET
  @Path("/variables")
  public List<Mica.DatasetVariableDto> getVariables() {
    checkPermission("/draft/harmonization-dataset", "VIEW");
    ImmutableList.Builder<Mica.DatasetVariableDto> builder = ImmutableList.builder();
    datasetService.getDatasetVariables(getDataset()).forEach(variable -> builder.add(dtos.asDto(variable)));
    return builder.build();
  }

  @Path("/variable/{variable}")
  public DraftDataschemaDatasetVariableResource getVariable(@PathParam("variable") String variable) {
    checkPermission("/draft/harmonization-dataset", "VIEW");
    DraftDataschemaDatasetVariableResource resource = applicationContext.getBean(DraftDataschemaDatasetVariableResource.class);
    resource.setDatasetId(id);
    resource.setVariableName(variable);
    return resource;
  }

  @Path("/study/{study}/variable/{variable}")
  public DraftHarmonizedDatasetVariableResource getVariable(@PathParam("study") String studyId, @PathParam("variable") String variable) {
    checkPermission("/draft/harmonization-dataset", "VIEW");
    DraftHarmonizedDatasetVariableResource resource = applicationContext.getBean(DraftHarmonizedDatasetVariableResource.class);
    resource.setDatasetId(id);
    resource.setVariableName(variable);
    resource.setStudyId(studyId);
    return resource;
  }

  @POST
  @Path("/facets")
  public List<Search.QueryResultDto> getFacets(Search.QueryTermsDto query) {
    checkPermission("/draft/harmonization-dataset", "VIEW");
    ImmutableList.Builder<Search.QueryResultDto> builder = ImmutableList.builder();
    HarmonizationDataset dataset = getDataset();
    for(OpalTable table : dataset.getAllOpalTables()) {
      builder.add(datasetService.getFacets(query, table));
    }
    return builder.build();
  }

  @PUT
  @Path("/_status")
  @Timed
  public Response updateStatus(@QueryParam("value") String status) {
    checkPermission("/draft/harmonization-dataset", "EDIT");
    datasetService.updateStatus(id, RevisionStatus.valueOf(status.toUpperCase()));

    return Response.noContent().build();
  }

  @GET
  @Path("/commit/{commitId}/view")
  public Mica.DatasetDto getFromCommit(@NotNull @PathParam("commitId") String commitId) throws IOException {
    checkPermission("/draft/harmonization-dataset", "VIEW");
    return dtos.asDto(datasetService.getFromCommit(datasetService.findDraft(id), commitId), true);
  }

  @Path("/permissions")
  public SubjectAclResource permissions() {
    SubjectAclResource subjectAclResource = applicationContext.getBean(SubjectAclResource.class);
    subjectAclResource.setResourceInstance("/draft/harmonization-dataset", id);
    subjectAclResource.setFileResourceInstance("/draft/file", "/harmonization-dataset/" + id);
    return subjectAclResource;
  }

  @Path("/accesses")
  public SubjectAclResource accesses() {
    SubjectAclResource subjectAclResource = applicationContext.getBean(SubjectAclResource.class);
    subjectAclResource.setResourceInstance("/harmonization-dataset", id);
    subjectAclResource.setFileResourceInstance("/file", "/harmonization-dataset/" + id);
    return subjectAclResource;
  }

  private HarmonizationDataset getDataset() {
    return datasetService.findById(id);
  }

  @Override
  protected String getId() {
    return id;
  }

  @Override
  protected AbstractGitPersistableService<HarmonizationDatasetState, HarmonizationDataset> getService() {
    return datasetService;
  }
}
