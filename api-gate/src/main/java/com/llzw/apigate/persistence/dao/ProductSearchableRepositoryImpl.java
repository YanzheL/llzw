package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.lucene.search.Query;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Pageable;

@SuppressWarnings("unchecked")
public class ProductSearchableRepositoryImpl implements ProductSearchableRepository {

  private static boolean indexed = false;

  @PersistenceContext
  private EntityManager entityManager;

  public List<Product> searchByNameOrIntroductionWithCustomQuery(String text,
      Pageable pageable) {
    init();
    Query combinedQuery = getQueryBuilder()
        .simpleQueryString()
        .onFields("name", "introduction")
        .matching(text)
        .createQuery();
    int page = pageable.getPageNumber();
    int size = pageable.getPageSize();
    return getJpaQuery(combinedQuery)
        .setFirstResult(page * size)
        .setMaxResults(size)
        .getResultList();
  }

  @Override
  public List<Product> searchByExample(Object example, Pageable pageable) {
    init();
    Query query = getQueryBuilder()
        .moreLikeThis()
        .excludeEntityUsedForComparison()
        .comparingAllFields()
        .toEntity(example)
        .createQuery();
    int page = pageable.getPageNumber();
    int size = pageable.getPageSize();
    return getJpaQuery(query)
        .setProjection(ProjectionConstants.THIS)
        .setFirstResult(page * size)
        .setMaxResults(size)
        .getResultList();
  }

  private FullTextQuery getJpaQuery(Query luceneQuery) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    return fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);
  }

  private QueryBuilder getQueryBuilder() {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    return fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder()
        .forEntity(Product.class)
        .get();
  }

  private void init() {
    if (indexed) {
      return;
    }
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    try {
      fullTextEntityManager.createIndexer().startAndWait();
      indexed = true;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
