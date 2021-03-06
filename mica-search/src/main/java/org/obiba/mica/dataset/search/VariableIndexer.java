/*
 * Copyright (c) 2017 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.dataset.search;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.obiba.mica.dataset.domain.Dataset;
import org.obiba.mica.dataset.domain.DatasetVariable;
import org.obiba.mica.dataset.domain.StudyDataset;
import org.obiba.mica.dataset.event.DatasetDeletedEvent;
import org.obiba.mica.dataset.event.DatasetPublishedEvent;
import org.obiba.mica.dataset.event.DatasetUnpublishedEvent;
import org.obiba.mica.dataset.service.CollectedDatasetService;
import org.obiba.mica.spi.search.Indexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;

/**
 * Indexer of variables, that reacts on dataset events.
 */
@Component
public class VariableIndexer {
  private static final Logger log = LoggerFactory.getLogger(VariableIndexer.class);

  @Inject
  private Indexer indexer;

  @Inject
  private CollectedDatasetService collectedDatasetService;

  @Async
  @Subscribe
  public void datasetPublished(DatasetPublishedEvent event) {
    log.debug("{} {} was published", event.getPersistable().getClass().getSimpleName(), event.getPersistable());
    clearDraftVariablesIndex();
    if(event.getVariables() != null) {
      deleteDatasetVariables(Indexer.PUBLISHED_VARIABLE_INDEX, event.getPersistable());

      if (event.getPersistable() instanceof StudyDataset) {
        indexDatasetVariables(Indexer.PUBLISHED_VARIABLE_INDEX, collectedDatasetService.processVariablesForStudyDataset(
          (StudyDataset) event.getPersistable(), event.getVariables()));
      } else {
        indexDatasetVariables(Indexer.PUBLISHED_VARIABLE_INDEX, event.getVariables());
      }
    }

    if(event.hasHarmonizationVariables())
      indexHarmonizedVariables(Indexer.PUBLISHED_VARIABLE_INDEX, event.getHarmonizationVariables());
  }

  @Async
  @Subscribe
  public void datasetUnpublished(DatasetUnpublishedEvent event) {
    log.debug("{} {} was unpublished", event.getPersistable().getClass().getSimpleName(), event.getPersistable());
    clearDraftVariablesIndex();
    deleteDatasetVariables(Indexer.PUBLISHED_VARIABLE_INDEX, event.getPersistable());
  }

  @Async
  @Subscribe
  public void datasetDeleted(DatasetDeletedEvent event) {
    log.debug("{} {} was deleted", event.getPersistable().getClass().getSimpleName(), event.getPersistable());
    clearDraftVariablesIndex();
    deleteDatasetVariables(Indexer.PUBLISHED_VARIABLE_INDEX, event.getPersistable());
  }

  //
  // Private methods
  //

  // legacy index, cleanup
  private void clearDraftVariablesIndex() {
    if(indexer.hasIndex(Indexer.DRAFT_VARIABLE_INDEX)) indexer.dropIndex(Indexer.DRAFT_VARIABLE_INDEX);
  }

  private void indexDatasetVariables(String indexName, Iterable<DatasetVariable> variables) {
    indexer.indexAllIndexables(indexName, variables);
  }

  protected void indexHarmonizedVariables(String indexName, Map<String, List<DatasetVariable>> harmonizationVariables) {
    harmonizationVariables.keySet().forEach(
      parentId -> indexer.indexAllIndexables(indexName, harmonizationVariables.get(parentId), parentId));
  }

  private void deleteDatasetVariables(String indexName, Dataset dataset) {
    // remove variables that have this dataset as parent
    Map.Entry<String, String> termQuery = ImmutablePair.of("datasetId", dataset.getId());
    indexer.delete(indexName, Indexer.HARMONIZED_VARIABLE_TYPE, termQuery);
    indexer.delete(indexName, Indexer.VARIABLE_TYPE, termQuery);
  }
}
