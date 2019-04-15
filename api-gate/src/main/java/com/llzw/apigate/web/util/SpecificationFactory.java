package com.llzw.apigate.web.util;

import com.llzw.apigate.persistence.dao.customquery.SearchCriterion;
import com.llzw.apigate.persistence.dao.customquery.SearchCriterionSpecificationFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

// TODO: It may lead to SQL injection.
public class SpecificationFactory {

//  Integer field1;
//  Double field2;
//  Float field3;

  public static <T, D> Specification<T> fromExample(D obj) throws IllegalAccessException {
    List<SearchCriterion> criteria = new ArrayList<>();
    Class c = obj.getClass();
    Field[] fields = c.getDeclaredFields();
    for (Field field : fields) {
      Object value = field.get(obj);
      if (value == null) {
        continue;
      }
      criteria.add(new SearchCriterion(field.getName(), "=", value.toString()));
    }
    return SearchCriterionSpecificationFactory.and(criteria);
  }

  public static <T, D> Specification<T> fromExample(D obj, Map<String, String> constraints)
      throws IllegalAccessException, NoSuchFieldException {
    List<SearchCriterion> criteria = new ArrayList<>();
    Class c = obj.getClass();
    for (Map.Entry<String, String> entry : constraints.entrySet()) {
      String fieldName = entry.getKey();
      String op = entry.getValue();
      Field field = c.getDeclaredField(fieldName);
      Object value = field.get(obj);
      if (value == null) {
        continue;
      }
      criteria.add(new SearchCriterion(fieldName, op, value.toString()));
    }
    return SearchCriterionSpecificationFactory.and(criteria);
  }

//  public static void main(String[] args) {
//    Map<String, String> constrains = new HashMap<>();
//    constrains.put("field1", ">=");
//    constrains.put("field2", "=");
//    try {
//      SpecificationFactory f = new SpecificationFactory();
//      f.field1 = 1;
//      f.field2 = 2.1;
//      fromExample(f);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
}
