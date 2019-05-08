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

public class ProductSearchableRepositoryImpl implements ProductSearchableRepository {

  private static boolean indexed = false;

  @PersistenceContext
  private EntityManager entityManager;

  public List<Product> searchByNameWithCustomQuery(String text) {
    init();
    Query query = getQueryBuilder()
        .simpleQueryString()
        .onFields("name")
        .matching(text)
        .createQuery();
    return getJpaQuery(query).getResultList();
  }

  public List<Product> searchByIntroductionWithCustomQuery(String text) {
    init();
    Query query = getQueryBuilder()
        .simpleQueryString()
        .onFields("introduction")
        .matching(text)
        .createQuery();
    return getJpaQuery(query).getResultList();
  }

  public List<Product> searchByNameOrIntroductionWithCustomQuery(String text) {
    init();
    Query combinedQuery = getQueryBuilder()
        .simpleQueryString()
        .onFields("name", "introduction")
        .matching(text)
        .createQuery();
    return getJpaQuery(combinedQuery).getResultList();
  }

  @Override
  public List<Product> searchByExample(Object example) {
    init();
    Query query = getQueryBuilder()
        .moreLikeThis()
        .excludeEntityUsedForComparison()
        .comparingAllFields()
        .toEntity(example)
        .createQuery();
    return getJpaQuery(query)
        .setProjection(ProjectionConstants.THIS)
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
