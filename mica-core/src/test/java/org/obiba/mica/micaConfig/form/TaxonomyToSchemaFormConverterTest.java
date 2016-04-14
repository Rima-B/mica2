package org.obiba.mica.micaConfig.form;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.obiba.mica.JSONUtils;
import org.obiba.mica.config.JsonConfiguration;
import org.obiba.mica.config.taxonomies.DatasetTaxonomy;
import org.obiba.mica.config.taxonomies.NetworkTaxonomy;
import org.obiba.mica.config.taxonomies.StudyTaxonomy;
import org.obiba.mica.config.taxonomies.TaxonomyTaxonomy;
import org.obiba.mica.config.taxonomies.VariableTaxonomy;
import org.obiba.mica.micaConfig.service.form.TaxonomyToSchemaFormConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TaxonomyToSchemaFormConverterTest.Config.class, JsonConfiguration.class })

public class TaxonomyToSchemaFormConverterTest {

  @Inject
  private StudyTaxonomy studyTaxonomy;

  @Test
  public void test_study_taxonomy_to_schema() throws JSONException {
    TaxonomyToSchemaFormConverter converter = new TaxonomyToSchemaFormConverter(studyTaxonomy);
    JSONObject schema = converter.toSchema("en");
    String schemaStr = schema.toString(2);
    assertThat(schemaStr).isNotEmpty();
  }

  @Test
  public void test_study_taxonomy_to_definition() throws JSONException {
    TaxonomyToSchemaFormConverter converter = new TaxonomyToSchemaFormConverter(studyTaxonomy);
    JSONArray definition = converter.toDefinition("en");
    String definitionStr = definition.toString(2);
    assertThat(definitionStr).isNotEmpty();
  }

  @Test
  public void test_to_map() throws IOException, JSONException {
    String json = "{\n" +
        "    \"objectives\": \"This is the objective.\",\n" +
        "    \"acronym\": \"XXX\",\n" +
        "    \"methods-designs\": \"cohort_study\",\n" +
        "    \"access\": \"bio_samples\",\n" +
        "    \"start\": 2000,\n" +
        "    \"end\": 2050,\n" +
        "    \"methods-recruitments\": \"individuals\",\n" +
        "    \"populations-dataCollectionEvents-administrativeDatabases\": [\n" +
        "        \"toto\",\n" +
        "        \"tutu\"\n" +
        "    ]\n" +
        "}";
    Map<String, Object> map = JSONUtils.toMap(json);
    String str = JSONUtils.toJSON(map);
    assertThat(new JSONObject(str).toString(2)).isEqualTo(new JSONObject(json).toString(2));
  }

  @Configuration
  @EnableConfigurationProperties({ NetworkTaxonomy.class, StudyTaxonomy.class, DatasetTaxonomy.class, VariableTaxonomy.class, TaxonomyTaxonomy.class })
  static class Config {

    @Inject
    private StudyTaxonomy studyTaxonomy;

    @Bean
    public StudyTaxonomy studyTaxonomy() {
      return studyTaxonomy;
    }

  }

}
