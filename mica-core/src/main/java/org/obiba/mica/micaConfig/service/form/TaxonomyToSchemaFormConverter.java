package org.obiba.mica.micaConfig.service.form;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.obiba.opal.core.domain.taxonomy.Taxonomy;
import org.obiba.opal.core.domain.taxonomy.TaxonomyEntity;
import org.obiba.opal.core.domain.taxonomy.Term;
import org.obiba.opal.core.domain.taxonomy.Vocabulary;

import com.google.common.base.Strings;

public class TaxonomyToSchemaFormConverter {

  private final Taxonomy taxonomy;

  public TaxonomyToSchemaFormConverter(Taxonomy taxonomy) {
    this.taxonomy = taxonomy;
  }

  public JSONObject toSchema(String locale) {
    JSONObject schema = new JSONObject();
    try {
      schema.put("type", "object");
      JSONObject properties = new JSONObject();
      if(taxonomy.hasVocabularies()) {
        for(Vocabulary vocabulary : taxonomy.getVocabularies()) {
          if(applies(vocabulary)) {
            properties.put(vocabulary.getName(), toSchema(vocabulary, locale));
          }
        }
      }
      schema.put("properties", properties);
      JSONArray required = new JSONArray();
      schema.put("required", required);
    } catch(JSONException e) {
      e.printStackTrace();
    }
    return schema;
  }

  public JSONArray toDefinition(String locale) {
    JSONArray definition = new JSONArray();
    if(taxonomy.hasVocabularies()) {
      taxonomy.getVocabularies().stream().filter(vocabulary -> applies(vocabulary)).forEach(vocabulary -> {
        definition.put(toDefinition(vocabulary, locale));
      });
    }
    return definition;
  }

  //
  // Private methods
  //

  private JSONObject toSchema(Vocabulary vocabulary, String locale) {
    JSONObject schema = new JSONObject();
    try {
      schema.put("title", vocabulary.getTitle().get(locale));

      String type = vocabulary.getAttributeValue("type");
      if (vocabulary.isRepeatable()) {
        type = "array";
      } else if(Strings.isNullOrEmpty(type)) {
        type = "string";
      } else if(isNumber(type)) {
        type = "number";
      } else {
        type = "string";
      }
      schema.put("type", type);
      if(vocabulary.hasTerms()) {
        JSONArray terms = new JSONArray();
        vocabulary.getTerms().stream().map(TaxonomyEntity::getName).forEach(t -> terms.put(t));
        if (vocabulary.isRepeatable()) {
          JSONObject items = new JSONObject();
          items.put("type", "string");
          items.put("enum", terms);
          schema.put("items", items);
        } else {
          schema.put("enum", terms);
        }

      }
    } catch(JSONException e) {
      e.printStackTrace();
    }
    return schema;
  }

  private JSONObject toDefinition(Vocabulary vocabulary, String locale) {
    JSONObject definition = new JSONObject();
    try {
      definition.put("key", vocabulary.getName());
      definition.put("title", vocabulary.getTitle().get(locale));
      if(vocabulary.hasTerms()) {
        definition.put("type", vocabulary.isRepeatable() ? "checkboxes" : (vocabulary.getTerms().size()>10 ? "select" : "radios"));
        JSONArray titleMap = new JSONArray();
        for(Term term : vocabulary.getTerms()) {
          JSONObject map = new JSONObject();
          map.put("value", term.getName());
          map.put("name", term.getTitle().get(locale));
          titleMap.put(map);
        }
        definition.put("titleMap", titleMap);
      }
    } catch(JSONException e) {
      e.printStackTrace();
    }
    return definition;
  }

  private boolean isNumber(String type) {
    return "integer".equals(type) || "decimal".equals(type);
  }

  private boolean applies(Vocabulary vocabulary) {
    if("id".equals(vocabulary.getName())) return false;
    String type = vocabulary.getAttributeValue("type");
    return isNumber(type) ? !vocabulary.hasTerms() : true;
  }

}
