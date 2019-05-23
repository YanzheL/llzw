package com.llzw.apigate.util;

import com.llzw.apigate.persistence.entity.Order;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DebugUtils {

  public static <T> String dumpSpec(Specification<T> specification, EntityManager entityManager,
      Class<T> tClass) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<T> root = cq.from(tClass);
    cq.where(specification.toPredicate(root, cq, cb));
    Query query = entityManager.createQuery(cq);
    return query.unwrap(org.hibernate.query.Query.class).getQueryString();
  }
}
