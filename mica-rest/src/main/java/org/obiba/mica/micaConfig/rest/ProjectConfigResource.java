/*
 * Copyright (c) 2017 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.micaConfig.rest;

import org.obiba.mica.micaConfig.domain.ProjectConfig;
import org.obiba.mica.micaConfig.service.EntityConfigService;
import org.obiba.mica.micaConfig.service.ProjectConfigService;
import org.obiba.mica.web.model.Dtos;
import org.obiba.mica.web.model.Mica;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Component
@Path("/config/project")
public class ProjectConfigResource
  extends EntityConfigResource<ProjectConfig, Mica.ProjectFormDto>
  implements PermissionAwareResource {

  @Inject
  ProjectConfigService projectConfigService;

  @Inject
  Dtos dtos;

  @Override
  public String getTarget() {
    return "project";
  }

  @Override
  protected Mica.ProjectFormDto asDto(ProjectConfig entityConfig) {
    return dtos.asDto(entityConfig);
  }

  @Override
  protected ProjectConfig fromDto(Mica.ProjectFormDto entityConfig) {
    return dtos.fromDto(entityConfig);
  }

  @Override
  protected EntityConfigService<ProjectConfig> getConfigService() {
    return projectConfigService;
  }
}
