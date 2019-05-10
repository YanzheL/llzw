package com.llzw.apigate.persistence.dao.customquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

// TODO: It may lead to SQL injection.
public class JpaSearchSpecificationFactory {

  public static <T> List<Specification<T>> of(List<JpaSearchCriterion> criteria) {
    List<Specification<T>> specifications = new ArrayList<>();
    for (JpaSearchCriterion criterion : criteria) {
      specifications.add(new JpaSearchSpecification<>(criterion));
    }
    return specifications;
  }

  public static <T> Specification<T> and(List<JpaSearchCriterion> criteria) {
    List<Specification<T>> specifications = of(criteria);
    Specification<T> cur = null;
    for (Specification<T> specification : specifications) {
      cur = cur == null ? specification : cur.and(specification);
    }
    return cur;
  }

  public static <T> Specification<T> or(List<JpaSearchCriterion> criteria) {
    List<Specification<T>> specifications = of(criteria);
    Specification<T> cur = null;
    for (Specification<T> specification : specifications) {
      cur = cur == null ? specification : cur.or(specification);
    }
    return cur;
  }

  /**
   * Make JPA Specification from an example POJO. For example:
   * <pre>
   * class SimplePojo {
   *   Type1 field1;
   *   Type2 field2;
   *   Type3 field3;
   * }
   * SimplePojo example = new SimplePojo();
   * example.field1 = value1;
   * example.field2 = value2;
   * example.field3 = null;
   * Specification specification = JpaSearchSpecificationFactory.fromExample(example);
   * </pre>
   * The result specification will contain JPA query constrains similar to:
   * <pre>
   *   WHERE field1 = value1 AND field2 = value2
   * </pre>
   * Fields with {@code null} value will be ignored.
   *
   * @param obj The example POJO, it can be any POJO
   * @param <T> Automatically inferred from caller.
   * @param <D> Automatically inferred from obj.
   * @return Constructed JPA Specification
   */
  public static <T, D> Specification<T> fromExample(D obj) throws IllegalAccessException {
    List<JpaSearchCriterion> criteria = new ArrayList<>();
    Class c = obj.getClass();
    Field[] fields = c.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      Object value = field.get(obj);
      if (value == null) {
        continue;
      }
      criteria.add(new JpaSearchCriterion(field.getName(), "=", value.toString()));
    }
    return and(criteria);
  }

  /**
   * Make JPA Specification from an example POJO with constrains. For example:
   * <pre>
   * SimplePojo example = new SimplePojo();
   * example.field1 = value1;
   * example.field2 = value2;
   * example.field3 = value3;
   * Map<String, String> constrains = new HashMap<>();
   * constrains.put("field1", ">=");
   * constrains.put("field2", "=");
   * Specification specification = JpaSearchSpecificationFactory.fromExample(example, constrains);
   * </pre>
   * The result specification will contain JPA query constrains similar to:
   * <pre>
   *   WHERE field1 >= value1 AND field2 = value2
   * </pre>
   * Fields with {@code null} value will be ignored.
   *
   * @param constraints field constrains map.
   * @see JpaSearchSpecificationFactory#fromExample(Object)
   */
  public static <T, D> Specification<T> fromExample(D obj, Map<String, String> constraints)
      throws IllegalAccessException, NoSuchFieldException {
    List<JpaSearchCriterion> criteria = new ArrayList<>();
    Class c = obj.getClass();
    for (Map.Entry<String, String> entry : constraints.entrySet()) {
      String fieldName = entry.getKey();
      String op = entry.getValue();
      Field field = c.getDeclaredField(fieldName);
      field.setAccessible(true);
      Object value = field.get(obj);
      if (value == null) {
        continue;
      }
      criteria.add(new JpaSearchCriterion(fieldName, op, value.toString()));
    }
    return and(criteria);
  }

//  Integer field1;
//  Double field2;
//  Float field3;

//  public static void main(String[] args) {
//    Map<String, String> constrains = new HashMap<>();
//    constrains.put("field1", ">=");
//    constrains.put("field2", "=");
//    try {
//      JpaSearchSpecificationFactory f = new JpaSearchSpecificationFactory();
//      f.field1 = 1;
//      f.field2 = 2.1;
//      fromExample(f);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
}
