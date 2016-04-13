package org.obiba.mica.micaConfig.form;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    assertThat(schema.toString(2)).isEqualTo("{\n" +
        "  \"properties\": {\n" +
        "    \"access\": {\n" +
        "      \"enum\": [\n" +
        "        \"data\",\n" +
        "        \"bio_samples\",\n" +
        "        \"other\"\n" +
        "      ],\n" +
        "      \"title\": \"Access\",\n" +
        "      \"type\": \"string\"\n" +
        "    },\n" +
        "    \"acronym\": {\n" +
        "      \"title\": \"Acronym\",\n" +
        "      \"type\": \"string\"\n" +
        "    },\n" +
        "    \"end\": {\n" +
        "      \"title\": \"End Year\",\n" +
        "      \"type\": \"number\"\n" +
        "    },\n" +
        "    \"methods-designs\": {\n" +
        "      \"enum\": [\n" +
        "        \"cohort_study\",\n" +
        "        \"case_control\",\n" +
        "        \"case_only\",\n" +
        "        \"cross_sectional\",\n" +
        "        \"clinical_trial\",\n" +
        "        \"other\"\n" +
        "      ],\n" +
        "      \"title\": \"Study designs\",\n" +
        "      \"type\": \"string\"\n" +
        "    },\n" +
        "    \"methods-recruitments\": {\n" +
        "      \"enum\": [\n" +
        "        \"individuals\",\n" +
        "        \"families\",\n" +
        "        \"other\"\n" +
        "      ],\n" +
        "      \"title\": \"Recruitment target\",\n" +
        "      \"type\": \"string\"\n" +
        "    },\n" +
        "    \"numberOfParticipants-participant-number\": {\n" +
        "      \"title\": \"Target number of participants\",\n" +
        "      \"type\": \"number\"\n" +
        "    },\n" +
        "    \"numberOfParticipants-sample-number\": {\n" +
        "      \"title\": \"Target number of participants with samples\",\n" +
        "      \"type\": \"number\"\n" +
        "    },\n" +
        "    \"objectives\": {\n" +
        "      \"title\": \"Objectives\",\n" +
        "      \"type\": \"string\"\n" +
        "    },\n" +
        "    \"populations-dataCollectionEvents-administrativeDatabases\": {\n" +
        "      \"items\": {\n" +
        "        \"enum\": [\n" +
        "          \"health_databases\",\n" +
        "          \"vital_statistics_databases\",\n" +
        "          \"socioeconomic_databases\",\n" +
        "          \"environmental_databases\"\n" +
        "        ],\n" +
        "        \"type\": \"string\"\n" +
        "      },\n" +
        "      \"title\": \"Data collection methods - Administrative databases\",\n" +
        "      \"type\": \"array\"\n" +
        "    },\n" +
        "    \"populations-dataCollectionEvents-bioSamples\": {\n" +
        "      \"items\": {\n" +
        "        \"enum\": [\n" +
        "          \"blood\",\n" +
        "          \"urine\",\n" +
        "          \"saliva\",\n" +
        "          \"hair\",\n" +
        "          \"others\",\n" +
        "          \"cord_blood\",\n" +
        "          \"tissues\",\n" +
        "          \"nail\",\n" +
        "          \"buccal_cells\"\n" +
        "        ],\n" +
        "        \"type\": \"string\"\n" +
        "      },\n" +
        "      \"title\": \"Data collection methods - Biosamples\",\n" +
        "      \"type\": \"array\"\n" +
        "    },\n" +
        "    \"populations-dataCollectionEvents-dataSources\": {\n" +
        "      \"items\": {\n" +
        "        \"enum\": [\n" +
        "          \"questionnaires\",\n" +
        "          \"physical_measures\",\n" +
        "          \"biological_samples\",\n" +
        "          \"administratives_databases\",\n" +
        "          \"others\"\n" +
        "        ],\n" +
        "        \"type\": \"string\"\n" +
        "      },\n" +
        "      \"title\": \"Data collection methods\",\n" +
        "      \"type\": \"array\"\n" +
        "    },\n" +
        "    \"populations-recruitment-dataSources\": {\n" +
        "      \"items\": {\n" +
        "        \"enum\": [\n" +
        "          \"general_population\",\n" +
        "          \"specific_population\",\n" +
        "          \"exist_studies\",\n" +
        "          \"other\"\n" +
        "        ],\n" +
        "        \"type\": \"string\"\n" +
        "      },\n" +
        "      \"title\": \"Recruitment source\",\n" +
        "      \"type\": \"array\"\n" +
        "    },\n" +
        "    \"populations-recruitment-generalPopulationSources\": {\n" +
        "      \"items\": {\n" +
        "        \"enum\": [\n" +
        "          \"volunteer\",\n" +
        "          \"selected_samples\",\n" +
        "          \"random\"\n" +
        "        ],\n" +
        "        \"type\": \"string\"\n" +
        "      },\n" +
        "      \"title\": \"Recruitment source - General population\",\n" +
        "      \"type\": \"array\"\n" +
        "    },\n" +
        "    \"populations-recruitment-specificPopulationSources\": {\n" +
        "      \"items\": {\n" +
        "        \"enum\": [\n" +
        "          \"clinic_patients\",\n" +
        "          \"specific_association\",\n" +
        "          \"other\"\n" +
        "        ],\n" +
        "        \"type\": \"string\"\n" +
        "      },\n" +
        "      \"title\": \"Recruitment source - Specific population\",\n" +
        "      \"type\": \"array\"\n" +
        "    },\n" +
        "    \"populations-selectionCriteria-ageMax\": {\n" +
        "      \"title\": \"Selection criteria - Maximum age\",\n" +
        "      \"type\": \"number\"\n" +
        "    },\n" +
        "    \"populations-selectionCriteria-ageMin\": {\n" +
        "      \"title\": \"Selection criteria - Minimum age\",\n" +
        "      \"type\": \"number\"\n" +
        "    },\n" +
        "    \"populations-selectionCriteria-countriesIso\": {\n" +
        "      \"items\": {\n" +
        "        \"enum\": [\n" +
        "          \"AND\",\n" +
        "          \"ARE\",\n" +
        "          \"AFG\",\n" +
        "          \"ATG\",\n" +
        "          \"AIA\",\n" +
        "          \"ALB\",\n" +
        "          \"ARM\",\n" +
        "          \"AGO\",\n" +
        "          \"ATA\",\n" +
        "          \"ARG\",\n" +
        "          \"ASM\",\n" +
        "          \"AUT\",\n" +
        "          \"AUS\",\n" +
        "          \"ABW\",\n" +
        "          \"ALA\",\n" +
        "          \"AZE\",\n" +
        "          \"BIH\",\n" +
        "          \"BRB\",\n" +
        "          \"BGD\",\n" +
        "          \"BEL\",\n" +
        "          \"BFA\",\n" +
        "          \"BGR\",\n" +
        "          \"BHR\",\n" +
        "          \"BDI\",\n" +
        "          \"BEN\",\n" +
        "          \"BLM\",\n" +
        "          \"BMU\",\n" +
        "          \"BRN\",\n" +
        "          \"BOL\",\n" +
        "          \"BES\",\n" +
        "          \"BRA\",\n" +
        "          \"BHS\",\n" +
        "          \"BTN\",\n" +
        "          \"BVT\",\n" +
        "          \"BWA\",\n" +
        "          \"BLR\",\n" +
        "          \"BLZ\",\n" +
        "          \"CAN\",\n" +
        "          \"CCK\",\n" +
        "          \"COD\",\n" +
        "          \"CAF\",\n" +
        "          \"COG\",\n" +
        "          \"CHE\",\n" +
        "          \"CIV\",\n" +
        "          \"COK\",\n" +
        "          \"CHL\",\n" +
        "          \"CMR\",\n" +
        "          \"CHN\",\n" +
        "          \"COL\",\n" +
        "          \"CRI\",\n" +
        "          \"CUB\",\n" +
        "          \"CPV\",\n" +
        "          \"CUW\",\n" +
        "          \"CXR\",\n" +
        "          \"CYP\",\n" +
        "          \"CZE\",\n" +
        "          \"DEU\",\n" +
        "          \"DJI\",\n" +
        "          \"DNK\",\n" +
        "          \"DMA\",\n" +
        "          \"DOM\",\n" +
        "          \"DZA\",\n" +
        "          \"ECU\",\n" +
        "          \"EST\",\n" +
        "          \"EGY\",\n" +
        "          \"ESH\",\n" +
        "          \"ERI\",\n" +
        "          \"ESP\",\n" +
        "          \"ETH\",\n" +
        "          \"FIN\",\n" +
        "          \"FJI\",\n" +
        "          \"FLK\",\n" +
        "          \"FSM\",\n" +
        "          \"FRO\",\n" +
        "          \"FRA\",\n" +
        "          \"GAB\",\n" +
        "          \"GBR\",\n" +
        "          \"GRD\",\n" +
        "          \"GEO\",\n" +
        "          \"GUF\",\n" +
        "          \"GGY\",\n" +
        "          \"GHA\",\n" +
        "          \"GIB\",\n" +
        "          \"GRL\",\n" +
        "          \"GMB\",\n" +
        "          \"GIN\",\n" +
        "          \"GLP\",\n" +
        "          \"GNQ\",\n" +
        "          \"GRC\",\n" +
        "          \"SGS\",\n" +
        "          \"GTM\",\n" +
        "          \"GUM\",\n" +
        "          \"GNB\",\n" +
        "          \"GUY\",\n" +
        "          \"HKG\",\n" +
        "          \"HMD\",\n" +
        "          \"HND\",\n" +
        "          \"HRV\",\n" +
        "          \"HTI\",\n" +
        "          \"HUN\",\n" +
        "          \"IDN\",\n" +
        "          \"IRL\",\n" +
        "          \"ISR\",\n" +
        "          \"IMN\",\n" +
        "          \"IND\",\n" +
        "          \"IOT\",\n" +
        "          \"IRQ\",\n" +
        "          \"IRN\",\n" +
        "          \"ISL\",\n" +
        "          \"ITA\",\n" +
        "          \"JEY\",\n" +
        "          \"JAM\",\n" +
        "          \"JOR\",\n" +
        "          \"JPN\",\n" +
        "          \"KEN\",\n" +
        "          \"KGZ\",\n" +
        "          \"KHM\",\n" +
        "          \"KIR\",\n" +
        "          \"COM\",\n" +
        "          \"KNA\",\n" +
        "          \"PRK\",\n" +
        "          \"KOR\",\n" +
        "          \"KWT\",\n" +
        "          \"CYM\",\n" +
        "          \"KAZ\",\n" +
        "          \"LAO\",\n" +
        "          \"LBN\",\n" +
        "          \"LCA\",\n" +
        "          \"LIE\",\n" +
        "          \"LKA\",\n" +
        "          \"LBR\",\n" +
        "          \"LSO\",\n" +
        "          \"LTU\",\n" +
        "          \"LUX\",\n" +
        "          \"LVA\",\n" +
        "          \"LBY\",\n" +
        "          \"MAR\",\n" +
        "          \"MCO\",\n" +
        "          \"MDA\",\n" +
        "          \"MNE\",\n" +
        "          \"MAF\",\n" +
        "          \"MDG\",\n" +
        "          \"MHL\",\n" +
        "          \"MKD\",\n" +
        "          \"MLI\",\n" +
        "          \"MMR\",\n" +
        "          \"MNG\",\n" +
        "          \"MAC\",\n" +
        "          \"MNP\",\n" +
        "          \"MTQ\",\n" +
        "          \"MRT\",\n" +
        "          \"MSR\",\n" +
        "          \"MLT\",\n" +
        "          \"MUS\",\n" +
        "          \"MDV\",\n" +
        "          \"MWI\",\n" +
        "          \"MEX\",\n" +
        "          \"MYS\",\n" +
        "          \"MOZ\",\n" +
        "          \"NAM\",\n" +
        "          \"NCL\",\n" +
        "          \"NER\",\n" +
        "          \"NFK\",\n" +
        "          \"NGA\",\n" +
        "          \"NIC\",\n" +
        "          \"NLD\",\n" +
        "          \"NOR\",\n" +
        "          \"NPL\",\n" +
        "          \"NRU\",\n" +
        "          \"NIU\",\n" +
        "          \"NZL\",\n" +
        "          \"OMN\",\n" +
        "          \"PAN\",\n" +
        "          \"PER\",\n" +
        "          \"PYF\",\n" +
        "          \"PNG\",\n" +
        "          \"PHL\",\n" +
        "          \"PAK\",\n" +
        "          \"POL\",\n" +
        "          \"SPM\",\n" +
        "          \"PCN\",\n" +
        "          \"PRI\",\n" +
        "          \"PSE\",\n" +
        "          \"PRT\",\n" +
        "          \"PLW\",\n" +
        "          \"PRY\",\n" +
        "          \"QAT\",\n" +
        "          \"REU\",\n" +
        "          \"ROU\",\n" +
        "          \"SRB\",\n" +
        "          \"RUS\",\n" +
        "          \"RWA\",\n" +
        "          \"SAU\",\n" +
        "          \"SLB\",\n" +
        "          \"SYC\",\n" +
        "          \"SDN\",\n" +
        "          \"SWE\",\n" +
        "          \"SGP\",\n" +
        "          \"SHN\",\n" +
        "          \"SVN\",\n" +
        "          \"SJM\",\n" +
        "          \"SVK\",\n" +
        "          \"SLE\",\n" +
        "          \"SMR\",\n" +
        "          \"SEN\",\n" +
        "          \"SOM\",\n" +
        "          \"SUR\",\n" +
        "          \"SSD\",\n" +
        "          \"STP\",\n" +
        "          \"SLV\",\n" +
        "          \"SXM\",\n" +
        "          \"SYR\",\n" +
        "          \"SWZ\",\n" +
        "          \"TCA\",\n" +
        "          \"TCD\",\n" +
        "          \"ATF\",\n" +
        "          \"TGO\",\n" +
        "          \"THA\",\n" +
        "          \"TJK\",\n" +
        "          \"TKL\",\n" +
        "          \"TLS\",\n" +
        "          \"TKM\",\n" +
        "          \"TUN\",\n" +
        "          \"TON\",\n" +
        "          \"TUR\",\n" +
        "          \"TTO\",\n" +
        "          \"TUV\",\n" +
        "          \"TWN\",\n" +
        "          \"TZA\",\n" +
        "          \"UKR\",\n" +
        "          \"UGA\",\n" +
        "          \"UMI\",\n" +
        "          \"USA\",\n" +
        "          \"URY\",\n" +
        "          \"UZB\",\n" +
        "          \"VAT\",\n" +
        "          \"VCT\",\n" +
        "          \"VEN\",\n" +
        "          \"VGB\",\n" +
        "          \"VIR\",\n" +
        "          \"VNM\",\n" +
        "          \"VUT\",\n" +
        "          \"WLF\",\n" +
        "          \"WSM\",\n" +
        "          \"YEM\",\n" +
        "          \"MYT\",\n" +
        "          \"ZAF\",\n" +
        "          \"ZMB\",\n" +
        "          \"ZWE\"\n" +
        "        ],\n" +
        "        \"type\": \"string\"\n" +
        "      },\n" +
        "      \"title\": \"Selection criteria - Country of residence\",\n" +
        "      \"type\": \"array\"\n" +
        "    },\n" +
        "    \"populations-selectionCriteria-criteria\": {\n" +
        "      \"enum\": [\n" +
        "        \"gravidity\",\n" +
        "        \"newborn\",\n" +
        "        \"twin\"\n" +
        "      ],\n" +
        "      \"title\": \"Selection criteria\",\n" +
        "      \"type\": \"string\"\n" +
        "    },\n" +
        "    \"populations-selectionCriteria-gender\": {\n" +
        "      \"enum\": [\n" +
        "        \"men\",\n" +
        "        \"women\"\n" +
        "      ],\n" +
        "      \"title\": \"Selection criteria - Gender\",\n" +
        "      \"type\": \"string\"\n" +
        "    },\n" +
        "    \"start\": {\n" +
        "      \"title\": \"Start Year\",\n" +
        "      \"type\": \"number\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"required\": [],\n" +
        "  \"type\": \"object\"\n" +
        "}");
  }

  @Test
  public void test_study_taxonomy_to_definition() throws JSONException {
    TaxonomyToSchemaFormConverter converter = new TaxonomyToSchemaFormConverter(studyTaxonomy);
    JSONArray definition = converter.toDefinition("en");
    assertThat(definition.toString(2)).isEqualTo("[\n" +
        "  {\n" +
        "    \"key\": \"objectives\",\n" +
        "    \"title\": \"Objectives\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"acronym\",\n" +
        "    \"title\": \"Acronym\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"methods-designs\",\n" +
        "    \"title\": \"Study designs\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Cohort study\",\n" +
        "        \"value\": \"cohort_study\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Case-control\",\n" +
        "        \"value\": \"case_control\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Case only\",\n" +
        "        \"value\": \"case_only\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cross-sectional\",\n" +
        "        \"value\": \"cross_sectional\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Clinical trial\",\n" +
        "        \"value\": \"clinical_trial\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Other\",\n" +
        "        \"value\": \"other\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"radios\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"access\",\n" +
        "    \"title\": \"Access\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Data\",\n" +
        "        \"value\": \"data\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Biosamples\",\n" +
        "        \"value\": \"bio_samples\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Other\",\n" +
        "        \"value\": \"other\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"radios\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"start\",\n" +
        "    \"title\": \"Start Year\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"end\",\n" +
        "    \"title\": \"End Year\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"methods-recruitments\",\n" +
        "    \"title\": \"Recruitment target\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Individuals\",\n" +
        "        \"value\": \"individuals\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Families\",\n" +
        "        \"value\": \"families\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Other\",\n" +
        "        \"value\": \"other\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"radios\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-recruitment-dataSources\",\n" +
        "    \"title\": \"Recruitment source\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"General population\",\n" +
        "        \"value\": \"general_population\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Specific population\",\n" +
        "        \"value\": \"specific_population\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Participants from existing studies\",\n" +
        "        \"value\": \"exist_studies\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Other\",\n" +
        "        \"value\": \"other\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"checkboxes\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-recruitment-generalPopulationSources\",\n" +
        "    \"title\": \"Recruitment source - General population\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Volunteer enrolment\",\n" +
        "        \"value\": \"volunteer\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Selected sample\",\n" +
        "        \"value\": \"selected_samples\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Random digit dialing\",\n" +
        "        \"value\": \"random\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"checkboxes\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-recruitment-specificPopulationSources\",\n" +
        "    \"title\": \"Recruitment source - Specific population\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Clinic patients\",\n" +
        "        \"value\": \"clinic_patients\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Members of an association\",\n" +
        "        \"value\": \"specific_association\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Other specific population\",\n" +
        "        \"value\": \"other\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"checkboxes\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-selectionCriteria-criteria\",\n" +
        "    \"title\": \"Selection criteria\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Gravidity\",\n" +
        "        \"value\": \"gravidity\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Newborn\",\n" +
        "        \"value\": \"newborn\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Twin\",\n" +
        "        \"value\": \"twin\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"radios\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-selectionCriteria-gender\",\n" +
        "    \"title\": \"Selection criteria - Gender\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Men only\",\n" +
        "        \"value\": \"men\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Women only\",\n" +
        "        \"value\": \"women\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"radios\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-selectionCriteria-countriesIso\",\n" +
        "    \"title\": \"Selection criteria - Country of residence\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Andorra\",\n" +
        "        \"value\": \"AND\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"United Arab Emirates\",\n" +
        "        \"value\": \"ARE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Afghanistan\",\n" +
        "        \"value\": \"AFG\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Antigua and Barbuda\",\n" +
        "        \"value\": \"ATG\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Anguilla\",\n" +
        "        \"value\": \"AIA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Albania\",\n" +
        "        \"value\": \"ALB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Armenia\",\n" +
        "        \"value\": \"ARM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Angola\",\n" +
        "        \"value\": \"AGO\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Antarctica\",\n" +
        "        \"value\": \"ATA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Argentina\",\n" +
        "        \"value\": \"ARG\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"American Samoa\",\n" +
        "        \"value\": \"ASM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Austria\",\n" +
        "        \"value\": \"AUT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Australia\",\n" +
        "        \"value\": \"AUS\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Aruba\",\n" +
        "        \"value\": \"ABW\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Åland Islands\",\n" +
        "        \"value\": \"ALA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Azerbaijan\",\n" +
        "        \"value\": \"AZE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bosnia and Herzegovina\",\n" +
        "        \"value\": \"BIH\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Barbados\",\n" +
        "        \"value\": \"BRB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bangladesh\",\n" +
        "        \"value\": \"BGD\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Belgium\",\n" +
        "        \"value\": \"BEL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Burkina Faso\",\n" +
        "        \"value\": \"BFA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bulgaria\",\n" +
        "        \"value\": \"BGR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bahrain\",\n" +
        "        \"value\": \"BHR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Burundi\",\n" +
        "        \"value\": \"BDI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Benin\",\n" +
        "        \"value\": \"BEN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saint Barthélemy\",\n" +
        "        \"value\": \"BLM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bermuda\",\n" +
        "        \"value\": \"BMU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Brunei Darussalam\",\n" +
        "        \"value\": \"BRN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bolivia, Plurinational State of\",\n" +
        "        \"value\": \"BOL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bonaire, Sint Eustatius and Saba\",\n" +
        "        \"value\": \"BES\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Brazil\",\n" +
        "        \"value\": \"BRA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bahamas\",\n" +
        "        \"value\": \"BHS\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bhutan\",\n" +
        "        \"value\": \"BTN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Bouvet Island\",\n" +
        "        \"value\": \"BVT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Botswana\",\n" +
        "        \"value\": \"BWA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Belarus\",\n" +
        "        \"value\": \"BLR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Belize\",\n" +
        "        \"value\": \"BLZ\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Canada\",\n" +
        "        \"value\": \"CAN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cocos (Keeling) Islands\",\n" +
        "        \"value\": \"CCK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Congo, the Democratic Republic of the\",\n" +
        "        \"value\": \"COD\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Central African Republic\",\n" +
        "        \"value\": \"CAF\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Congo\",\n" +
        "        \"value\": \"COG\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Switzerland\",\n" +
        "        \"value\": \"CHE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Côte d'Ivoire\",\n" +
        "        \"value\": \"CIV\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cook Islands\",\n" +
        "        \"value\": \"COK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Chile\",\n" +
        "        \"value\": \"CHL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cameroon\",\n" +
        "        \"value\": \"CMR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"China\",\n" +
        "        \"value\": \"CHN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Colombia\",\n" +
        "        \"value\": \"COL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Costa Rica\",\n" +
        "        \"value\": \"CRI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cuba\",\n" +
        "        \"value\": \"CUB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cabo Verde\",\n" +
        "        \"value\": \"CPV\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Curaçao\",\n" +
        "        \"value\": \"CUW\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Christmas Island\",\n" +
        "        \"value\": \"CXR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cyprus\",\n" +
        "        \"value\": \"CYP\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Czech Republic\",\n" +
        "        \"value\": \"CZE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Germany\",\n" +
        "        \"value\": \"DEU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Djibouti\",\n" +
        "        \"value\": \"DJI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Denmark\",\n" +
        "        \"value\": \"DNK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Dominica\",\n" +
        "        \"value\": \"DMA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Dominican Republic\",\n" +
        "        \"value\": \"DOM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Algeria\",\n" +
        "        \"value\": \"DZA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Ecuador\",\n" +
        "        \"value\": \"ECU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Estonia\",\n" +
        "        \"value\": \"EST\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Egypt\",\n" +
        "        \"value\": \"EGY\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Western Sahara\",\n" +
        "        \"value\": \"ESH\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Eritrea\",\n" +
        "        \"value\": \"ERI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Spain\",\n" +
        "        \"value\": \"ESP\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Ethiopia\",\n" +
        "        \"value\": \"ETH\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Finland\",\n" +
        "        \"value\": \"FIN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Fiji\",\n" +
        "        \"value\": \"FJI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Falkland Islands (Malvinas)\",\n" +
        "        \"value\": \"FLK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Micronesia, Federated States of\",\n" +
        "        \"value\": \"FSM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Faroe Islands\",\n" +
        "        \"value\": \"FRO\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"France\",\n" +
        "        \"value\": \"FRA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Gabon\",\n" +
        "        \"value\": \"GAB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"United Kingdom of Great Britain and Northern Ireland\",\n" +
        "        \"value\": \"GBR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Grenada\",\n" +
        "        \"value\": \"GRD\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Georgia\",\n" +
        "        \"value\": \"GEO\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"French Guiana\",\n" +
        "        \"value\": \"GUF\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Guernsey\",\n" +
        "        \"value\": \"GGY\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Ghana\",\n" +
        "        \"value\": \"GHA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Gibraltar\",\n" +
        "        \"value\": \"GIB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Greenland\",\n" +
        "        \"value\": \"GRL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Gambia\",\n" +
        "        \"value\": \"GMB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Guinea\",\n" +
        "        \"value\": \"GIN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Guadeloupe\",\n" +
        "        \"value\": \"GLP\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Equatorial Guinea\",\n" +
        "        \"value\": \"GNQ\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Greece\",\n" +
        "        \"value\": \"GRC\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"South Georgia and the South Sandwich Islands\",\n" +
        "        \"value\": \"SGS\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Guatemala\",\n" +
        "        \"value\": \"GTM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Guam\",\n" +
        "        \"value\": \"GUM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Guinea-Bissau\",\n" +
        "        \"value\": \"GNB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Guyana\",\n" +
        "        \"value\": \"GUY\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Hong Kong\",\n" +
        "        \"value\": \"HKG\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Heard Island and McDonald Islands\",\n" +
        "        \"value\": \"HMD\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Honduras\",\n" +
        "        \"value\": \"HND\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Croatia\",\n" +
        "        \"value\": \"HRV\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Haiti\",\n" +
        "        \"value\": \"HTI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Hungary\",\n" +
        "        \"value\": \"HUN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Indonesia\",\n" +
        "        \"value\": \"IDN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Ireland\",\n" +
        "        \"value\": \"IRL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Israel\",\n" +
        "        \"value\": \"ISR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Isle of Man\",\n" +
        "        \"value\": \"IMN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"India\",\n" +
        "        \"value\": \"IND\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"British Indian Ocean Territory\",\n" +
        "        \"value\": \"IOT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Iraq\",\n" +
        "        \"value\": \"IRQ\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Iran, Islamic Republic of\",\n" +
        "        \"value\": \"IRN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Iceland\",\n" +
        "        \"value\": \"ISL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Italy\",\n" +
        "        \"value\": \"ITA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Jersey\",\n" +
        "        \"value\": \"JEY\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Jamaica\",\n" +
        "        \"value\": \"JAM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Jordan\",\n" +
        "        \"value\": \"JOR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Japan\",\n" +
        "        \"value\": \"JPN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Kenya\",\n" +
        "        \"value\": \"KEN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Kyrgyzstan\",\n" +
        "        \"value\": \"KGZ\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cambodia\",\n" +
        "        \"value\": \"KHM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Kiribati\",\n" +
        "        \"value\": \"KIR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Comoros\",\n" +
        "        \"value\": \"COM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saint Kitts and Nevis\",\n" +
        "        \"value\": \"KNA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Korea, Democratic People's Republic of\",\n" +
        "        \"value\": \"PRK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Korea, Republic of\",\n" +
        "        \"value\": \"KOR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Kuwait\",\n" +
        "        \"value\": \"KWT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cayman Islands\",\n" +
        "        \"value\": \"CYM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Kazakhstan\",\n" +
        "        \"value\": \"KAZ\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Lao People's Democratic Republic\",\n" +
        "        \"value\": \"LAO\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Lebanon\",\n" +
        "        \"value\": \"LBN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saint Lucia\",\n" +
        "        \"value\": \"LCA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Liechtenstein\",\n" +
        "        \"value\": \"LIE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Sri Lanka\",\n" +
        "        \"value\": \"LKA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Liberia\",\n" +
        "        \"value\": \"LBR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Lesotho\",\n" +
        "        \"value\": \"LSO\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Lithuania\",\n" +
        "        \"value\": \"LTU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Luxembourg\",\n" +
        "        \"value\": \"LUX\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Latvia\",\n" +
        "        \"value\": \"LVA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Libya\",\n" +
        "        \"value\": \"LBY\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Morocco\",\n" +
        "        \"value\": \"MAR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Monaco\",\n" +
        "        \"value\": \"MCO\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Moldova, Republic of\",\n" +
        "        \"value\": \"MDA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Montenegro\",\n" +
        "        \"value\": \"MNE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saint Martin (French part)\",\n" +
        "        \"value\": \"MAF\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Madagascar\",\n" +
        "        \"value\": \"MDG\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Marshall Islands\",\n" +
        "        \"value\": \"MHL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Macedonia, the former Yugoslav Republic of\",\n" +
        "        \"value\": \"MKD\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Mali\",\n" +
        "        \"value\": \"MLI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Myanmar\",\n" +
        "        \"value\": \"MMR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Mongolia\",\n" +
        "        \"value\": \"MNG\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Macao\",\n" +
        "        \"value\": \"MAC\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Northern Mariana Islands\",\n" +
        "        \"value\": \"MNP\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Martinique\",\n" +
        "        \"value\": \"MTQ\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Mauritania\",\n" +
        "        \"value\": \"MRT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Montserrat\",\n" +
        "        \"value\": \"MSR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Malta\",\n" +
        "        \"value\": \"MLT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Mauritius\",\n" +
        "        \"value\": \"MUS\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Maldives\",\n" +
        "        \"value\": \"MDV\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Malawi\",\n" +
        "        \"value\": \"MWI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Mexico\",\n" +
        "        \"value\": \"MEX\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Malaysia\",\n" +
        "        \"value\": \"MYS\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Mozambique\",\n" +
        "        \"value\": \"MOZ\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Namibia\",\n" +
        "        \"value\": \"NAM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"New Caledonia\",\n" +
        "        \"value\": \"NCL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Niger\",\n" +
        "        \"value\": \"NER\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Norfolk Island\",\n" +
        "        \"value\": \"NFK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Nigeria\",\n" +
        "        \"value\": \"NGA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Nicaragua\",\n" +
        "        \"value\": \"NIC\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Netherlands\",\n" +
        "        \"value\": \"NLD\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Norway\",\n" +
        "        \"value\": \"NOR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Nepal\",\n" +
        "        \"value\": \"NPL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Nauru\",\n" +
        "        \"value\": \"NRU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Niue\",\n" +
        "        \"value\": \"NIU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"New Zealand\",\n" +
        "        \"value\": \"NZL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Oman\",\n" +
        "        \"value\": \"OMN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Panama\",\n" +
        "        \"value\": \"PAN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Peru\",\n" +
        "        \"value\": \"PER\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"French Polynesia\",\n" +
        "        \"value\": \"PYF\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Papua New Guinea\",\n" +
        "        \"value\": \"PNG\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Philippines\",\n" +
        "        \"value\": \"PHL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Pakistan\",\n" +
        "        \"value\": \"PAK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Poland\",\n" +
        "        \"value\": \"POL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saint Pierre and Miquelon\",\n" +
        "        \"value\": \"SPM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Pitcairn\",\n" +
        "        \"value\": \"PCN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Puerto Rico\",\n" +
        "        \"value\": \"PRI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Palestine, State of\",\n" +
        "        \"value\": \"PSE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Portugal\",\n" +
        "        \"value\": \"PRT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Palau\",\n" +
        "        \"value\": \"PLW\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Paraguay\",\n" +
        "        \"value\": \"PRY\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Qatar\",\n" +
        "        \"value\": \"QAT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Réunion\",\n" +
        "        \"value\": \"REU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Romania\",\n" +
        "        \"value\": \"ROU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Serbia\",\n" +
        "        \"value\": \"SRB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Russian Federation\",\n" +
        "        \"value\": \"RUS\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Rwanda\",\n" +
        "        \"value\": \"RWA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saudi Arabia\",\n" +
        "        \"value\": \"SAU\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Solomon Islands\",\n" +
        "        \"value\": \"SLB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Seychelles\",\n" +
        "        \"value\": \"SYC\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Sudan\",\n" +
        "        \"value\": \"SDN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Sweden\",\n" +
        "        \"value\": \"SWE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Singapore\",\n" +
        "        \"value\": \"SGP\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saint Helena, Ascension and Tristan da Cunha\",\n" +
        "        \"value\": \"SHN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Slovenia\",\n" +
        "        \"value\": \"SVN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Svalbard and Jan Mayen\",\n" +
        "        \"value\": \"SJM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Slovakia\",\n" +
        "        \"value\": \"SVK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Sierra Leone\",\n" +
        "        \"value\": \"SLE\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"San Marino\",\n" +
        "        \"value\": \"SMR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Senegal\",\n" +
        "        \"value\": \"SEN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Somalia\",\n" +
        "        \"value\": \"SOM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Suriname\",\n" +
        "        \"value\": \"SUR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"South Sudan\",\n" +
        "        \"value\": \"SSD\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Sao Tome and Principe\",\n" +
        "        \"value\": \"STP\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"El Salvador\",\n" +
        "        \"value\": \"SLV\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Sint Maarten (Dutch part)\",\n" +
        "        \"value\": \"SXM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Syrian Arab Republic\",\n" +
        "        \"value\": \"SYR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Swaziland\",\n" +
        "        \"value\": \"SWZ\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Turks and Caicos Islands\",\n" +
        "        \"value\": \"TCA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Chad\",\n" +
        "        \"value\": \"TCD\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"French Southern Territories\",\n" +
        "        \"value\": \"ATF\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Togo\",\n" +
        "        \"value\": \"TGO\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Thailand\",\n" +
        "        \"value\": \"THA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Tajikistan\",\n" +
        "        \"value\": \"TJK\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Tokelau\",\n" +
        "        \"value\": \"TKL\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Timor-Leste\",\n" +
        "        \"value\": \"TLS\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Turkmenistan\",\n" +
        "        \"value\": \"TKM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Tunisia\",\n" +
        "        \"value\": \"TUN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Tonga\",\n" +
        "        \"value\": \"TON\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Turkey\",\n" +
        "        \"value\": \"TUR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Trinidad and Tobago\",\n" +
        "        \"value\": \"TTO\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Tuvalu\",\n" +
        "        \"value\": \"TUV\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Taiwan, Province of China\",\n" +
        "        \"value\": \"TWN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Tanzania, United Republic of\",\n" +
        "        \"value\": \"TZA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Ukraine\",\n" +
        "        \"value\": \"UKR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Uganda\",\n" +
        "        \"value\": \"UGA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"United States Minor Outlying Islands\",\n" +
        "        \"value\": \"UMI\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"United States of America\",\n" +
        "        \"value\": \"USA\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Uruguay\",\n" +
        "        \"value\": \"URY\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Uzbekistan\",\n" +
        "        \"value\": \"UZB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Holy See\",\n" +
        "        \"value\": \"VAT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saint Vincent and the Grenadines\",\n" +
        "        \"value\": \"VCT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Venezuela, Bolivarian Republic of\",\n" +
        "        \"value\": \"VEN\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Virgin Islands, British\",\n" +
        "        \"value\": \"VGB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Virgin Islands, U.S.\",\n" +
        "        \"value\": \"VIR\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Viet Nam\",\n" +
        "        \"value\": \"VNM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Vanuatu\",\n" +
        "        \"value\": \"VUT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Wallis and Futuna\",\n" +
        "        \"value\": \"WLF\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Samoa\",\n" +
        "        \"value\": \"WSM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Yemen\",\n" +
        "        \"value\": \"YEM\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Mayotte\",\n" +
        "        \"value\": \"MYT\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"South Africa\",\n" +
        "        \"value\": \"ZAF\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Zambia\",\n" +
        "        \"value\": \"ZMB\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Zimbabwe\",\n" +
        "        \"value\": \"ZWE\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"checkboxes\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-selectionCriteria-ageMin\",\n" +
        "    \"title\": \"Selection criteria - Minimum age\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-selectionCriteria-ageMax\",\n" +
        "    \"title\": \"Selection criteria - Maximum age\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"numberOfParticipants-participant-number\",\n" +
        "    \"title\": \"Target number of participants\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"numberOfParticipants-sample-number\",\n" +
        "    \"title\": \"Target number of participants with samples\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-dataCollectionEvents-dataSources\",\n" +
        "    \"title\": \"Data collection methods\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Questionnaires\",\n" +
        "        \"value\": \"questionnaires\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Physical Measures\",\n" +
        "        \"value\": \"physical_measures\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Biosamples\",\n" +
        "        \"value\": \"biological_samples\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Administrative Databases\",\n" +
        "        \"value\": \"administratives_databases\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Others\",\n" +
        "        \"value\": \"others\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"checkboxes\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-dataCollectionEvents-bioSamples\",\n" +
        "    \"title\": \"Data collection methods - Biosamples\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Blood\",\n" +
        "        \"value\": \"blood\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Urine\",\n" +
        "        \"value\": \"urine\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Saliva\",\n" +
        "        \"value\": \"saliva\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Hair\",\n" +
        "        \"value\": \"hair\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Others\",\n" +
        "        \"value\": \"others\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Cord Blood\",\n" +
        "        \"value\": \"cord_blood\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Tissues\",\n" +
        "        \"value\": \"tissues\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Nail\",\n" +
        "        \"value\": \"nail\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Buccal Cells\",\n" +
        "        \"value\": \"buccal_cells\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"checkboxes\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"key\": \"populations-dataCollectionEvents-administrativeDatabases\",\n" +
        "    \"title\": \"Data collection methods - Administrative databases\",\n" +
        "    \"titleMap\": [\n" +
        "      {\n" +
        "        \"name\": \"Health databases\",\n" +
        "        \"value\": \"health_databases\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Vital Statistics Databases\",\n" +
        "        \"value\": \"vital_statistics_databases\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Socioeconomic Databases\",\n" +
        "        \"value\": \"socioeconomic_databases\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"Environmental Databases\",\n" +
        "        \"value\": \"environmental_databases\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"type\": \"checkboxes\"\n" +
        "  }\n" +
        "]");
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
