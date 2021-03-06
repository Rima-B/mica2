/*
 * Copyright (c) 2017 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.project.search;

import com.google.common.eventbus.Subscribe;
import org.obiba.mica.project.domain.Project;
import org.obiba.mica.project.event.*;
import org.obiba.mica.project.service.ProjectService;
import org.obiba.mica.spi.search.Indexable;
import org.obiba.mica.spi.search.Indexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class ProjectIndexer {

  private static final Logger log = LoggerFactory.getLogger(ProjectIndexer.class);

  @Inject
  private ProjectService projectService;

  @Inject
  private Indexer indexer;

  @Async
  @Subscribe
  public void networkUpdated(ProjectUpdatedEvent event) {
    log.info("Project {} was updated", (Indexable) event.getPersistable());
    indexer.index(Indexer.DRAFT_PROJECT_INDEX, (Indexable) event.getPersistable());
  }

  @Async
  @Subscribe
  public void networkPublished(ProjectPublishedEvent event) {
    log.info("Project {} was published", (Indexable) event.getPersistable());
    indexer.index(Indexer.PUBLISHED_PROJECT_INDEX, (Indexable) event.getPersistable());
  }

  @Async
  @Subscribe
  public void networkPublished(ProjectUnpublishedEvent event) {
    log.info("Project {} was unpublished", (Indexable) event.getPersistable());
    indexer.delete(Indexer.PUBLISHED_PROJECT_INDEX, (Indexable) event.getPersistable());
  }

  @Async
  @Subscribe
  public void networkDeleted(ProjectDeletedEvent event) {
    log.info("Project {} was deleted", (Indexable) event.getPersistable());
    indexer.delete(Indexer.DRAFT_PROJECT_INDEX, (Indexable) event.getPersistable());
    indexer.delete(Indexer.PUBLISHED_PROJECT_INDEX, (Indexable) event.getPersistable());
  }

  @Async
  @Subscribe
  public void reIndexProjects(IndexProjectsEvent event) {
    log.info("Reindexing all networks");
    reIndexAll(Indexer.PUBLISHED_PROJECT_INDEX, projectService.findAllPublishedProjects());
    reIndexAll(Indexer.DRAFT_PROJECT_INDEX, projectService.findAllProjects());
  }

  private void reIndexAll(String indexName, Iterable<Project> projects) {
    indexer.reIndexAllIndexables(indexName, projects);
  }
}
