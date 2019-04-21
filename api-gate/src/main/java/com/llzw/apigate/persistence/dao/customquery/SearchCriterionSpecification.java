package com.llzw.apigate.persistence.dao.customquery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SearchCriterionSpecification<T> implements Specification<T> {

  SearchCriterion criterion;

  public SearchCriterionSpecification(SearchCriterion criterion) {
    this.criterion = criterion;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    if (criterion.getOperation().equalsIgnoreCase(">")) {
      return builder.greaterThanOrEqualTo(
          root.get(criterion.getKey()), criterion.getValue().toString());
    } else if (criterion.getOperation().equalsIgnoreCase("<")) {
      return builder.lessThanOrEqualTo(
          root.get(criterion.getKey()), criterion.getValue().toString());
    } else if (criterion.getOperation().equalsIgnoreCase(":")) {
      if (root.get(criterion.getKey()).getJavaType() == String.class) {
        return builder.like(root.get(criterion.getKey()), "%" + criterion.getValue() + "%");
      } else {
        return builder.equal(root.get(criterion.getKey()), criterion.getValue());
      }
    }
    return null;
  }
}
