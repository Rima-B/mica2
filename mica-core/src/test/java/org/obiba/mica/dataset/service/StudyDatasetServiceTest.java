package org.obiba.mica.dataset.service;

import java.util.Locale;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.obiba.magma.MagmaRuntimeException;
import org.obiba.magma.NoSuchValueTableException;
import org.obiba.mica.core.domain.LocalizedString;
import org.obiba.mica.core.domain.StudyTable;
import org.obiba.mica.dataset.StudyDatasetRepository;
import org.obiba.mica.dataset.domain.StudyDataset;
import org.obiba.mica.micaConfig.OpalService;
import org.obiba.mica.study.domain.Study;
import org.obiba.mica.study.service.StudyService;

import com.google.common.eventbus.EventBus;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.obiba.opal.rest.client.magma.RestDatasource;


public class StudyDatasetServiceTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @InjectMocks
  private StudyDatasetService studyDatasetService;

  @Mock
  private StudyService studyService;

  @Mock
  private OpalService opalService;

  @Mock
  private StudyDatasetRepository studyDatasetRepository;

  @Mock
  private EventBus eventBus;

  private Study study;

  private StudyDataset dataset;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    study = buildStudy();
    dataset = buildStudyDataset();
  }

  @Test
  public void testDatasourceConnectionError() {
    RestDatasource r = mock(RestDatasource.class);
    when(r.getValueTable(anyString())).thenThrow(new MagmaRuntimeException());
    when(opalService.getDatasource(anyString(), anyString())).thenReturn(r);
    when(studyService.findDraftStudy(anyString())).thenReturn(study);

    exception.expect(DatasourceNotAvailableException.class);

    studyDatasetService.save(dataset);
  }

  @Test
  public void testInvalidValueTableInDataset() {
    RestDatasource r = mock(RestDatasource.class);
    when(r.getValueTable(anyString())).thenThrow(NoSuchValueTableException.class);
    when(opalService.getDatasource(anyString(), anyString())).thenReturn(r);
    when(studyService.findDraftStudy(anyString())).thenReturn(study);

    exception.expect(InvalidDatasetException.class);

    studyDatasetService.save(dataset);
  }

  private StudyDataset buildStudyDataset() {
    StudyDataset ds = new StudyDataset();
    StudyTable st = new StudyTable();
    st.setProject("proj");
    st.setTable("tab");
    ds.setStudyTable(st);
    ds.setName(new LocalizedString(Locale.CANADA, "test"));

    return ds;
  }

  private Study buildStudy() {
    Study s = new Study();
    s.setOpal("opal");

    return s;
  }
}