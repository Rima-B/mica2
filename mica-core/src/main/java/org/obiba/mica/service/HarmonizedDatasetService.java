/*
 * Copyright (c) 2014 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.obiba.magma.NoSuchValueTableException;
import org.obiba.mica.domain.HarmonizedDataset;
import org.obiba.mica.repository.HarmonizedDatasetRepository;
import org.obiba.mica.study.event.StudyDeletedEvent;
import org.obiba.opal.rest.client.magma.RestValueTable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;

@Service
@Validated
public class HarmonizedDatasetService extends DatasetService {

  @Inject
  private HarmonizedDatasetRepository harmonizedDatasetRepository;

  public void save(@NotNull HarmonizedDataset dataset) {
    harmonizedDatasetRepository.save(dataset);
  }

  /**
   * Get the {@link org.obiba.mica.domain.HarmonizedDataset} from its id.
   *
   * @param id
   * @return
   * @throws org.obiba.mica.service.NoSuchDatasetException
   */
  @NotNull
  public HarmonizedDataset findById(@NotNull String id) throws NoSuchDatasetException {
    HarmonizedDataset dataset = harmonizedDatasetRepository.findOne(id);
    if(dataset == null) throw NoSuchDatasetException.withId(id);
    return dataset;
  }

  /**
   * Get all {@link org.obiba.mica.domain.HarmonizedDataset}s.
   *
   * @return
   */
  public List<HarmonizedDataset> findAllDatasets() {
    return harmonizedDatasetRepository.findAll();
  }

  /**
   * Get all {@link org.obiba.mica.domain.HarmonizedDataset}s having a reference to the given study.
   *
   * @param studyId
   * @return
   */
  public List<HarmonizedDataset> findAllDatasets(String studyId) {
    if(Strings.isNullOrEmpty(studyId)) return findAllDatasets();
    return harmonizedDatasetRepository.findByStudyTablesStudyId(studyId);
  }

  /**
   * Get all published {@link org.obiba.mica.domain.HarmonizedDataset}s.
   *
   * @return
   */
  public List<HarmonizedDataset> findAllPublishedDatasets() {
    return harmonizedDatasetRepository.findByPublished(true);
  }

  /**
   * Get all published {@link org.obiba.mica.domain.HarmonizedDataset}s having a reference to the given study.
   *
   * @param studyId
   * @return
   */
  public List<HarmonizedDataset> findAllPublishedDatasets(String studyId) {
    if(Strings.isNullOrEmpty(studyId)) return findAllPublishedDatasets();
    return harmonizedDatasetRepository.findByStudyTablesStudyIdAndPublished(studyId, true);
  }

  /**
   * Apply dataset publication flag.
   * @param studyId
   * @param published
   */
  public void publish(String studyId, boolean published) {
    HarmonizedDataset dataset = findById(studyId);
    dataset.setPublished(published);
    save(dataset);
  }

  /**
   * Check if a dataset is published.
   * @param studyId
   * @return
   */
  public boolean isPublished(String studyId) throws NoSuchDatasetException {
    HarmonizedDataset dataset = findById(studyId);
    return dataset.isPublished();
  }

  @Override
  @NotNull
  public RestValueTable getTable(@NotNull String id) throws NoSuchDatasetException, NoSuchValueTableException {
    HarmonizedDataset dataset = findById(id);
    return execute(dataset.getProject(), datasource -> (RestValueTable) datasource.getValueTable(dataset.getTable()));
  }

  /**
   * On study deletion, go through all datasets related to this study and remove the dependency.
   *
   * @param event
   */
  @Async
  @Subscribe
  public void studyDeleted(StudyDeletedEvent event) {

  }

  //
  // Private methods
  //

  /**
   * Build or reuse the {@link org.obiba.opal.rest.client.magma.RestDatasource} and execute the callback with it.
   *
   * @param project
   * @param callback
   * @param <T>
   * @return
   */
  private <T> T execute(String project, DatasourceCallback<T> callback) {
    return execute(getDatasource(project), callback);
  }

}
