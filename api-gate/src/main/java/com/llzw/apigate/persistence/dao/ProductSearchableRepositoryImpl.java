package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

public class ProductSearchableRepositoryImpl implements ProductSearchableRepository {

  @PersistenceContext
  private EntityManager entityManager;

  public List<Product> searchByNameWithCustomQuery(String text) {
    Query query = getQueryBuilder()
        .simpleQueryString()
        .onFields("name")
        .matching(text)
        .createQuery();
    return getJpaQuery(query).getResultList();
  }

  public List<Product> searchByIntroductionWithCustomQuery(String text) {
    Query query = getQueryBuilder()
        .simpleQueryString()
        .onFields("introduction")
        .matching(text)
        .createQuery();
    return getJpaQuery(query).getResultList();
  }

  public List<Product> searchByNameOrIntroductionWithCustomQuery(String text) {
    Query combinedQuery = getQueryBuilder()
        .simpleQueryString()
        .onFields("name", "introduction")
        .matching(text)
        .createQuery();
    return getJpaQuery(combinedQuery).getResultList();
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
}
