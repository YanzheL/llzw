package com.llzw.apigate.persistence.dao.customquery;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class SearchCriterionSpecificationFactory {

  public static <T> List<Specification<T>> of(List<SearchCriterion> criteria) {
    List<Specification<T>> specifications = new ArrayList<>();
    for (SearchCriterion criterion : criteria) {
      specifications.add(new SearchCriterionSpecification<>(criterion));
    }
    return specifications;
  }

  public static <T> Specification<T> and(List<SearchCriterion> criteria) {
    List<Specification<T>> specifications = of(criteria);
    Specification<T> cur = null;
    for (Specification<T> specification : specifications) {
      cur = cur == null ? specification : cur.and(specification);
    }
    return cur;
  }

  public static <T> Specification<T> or(List<SearchCriterion> criteria) {
    List<Specification<T>> specifications = of(criteria);
    Specification<T> cur = null;
    for (Specification<T> specification : specifications) {
      cur = cur == null ? specification : cur.or(specification);
    }
    return cur;
  }
}
