package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import java.util.List;

public interface ProductSearchableRepository {

  List<Product> searchByNameWithCustomQuery(String text);

  List<Product> searchByIntroductionWithCustomQuery(String text);

  List<Product> searchByNameOrIntroductionWithCustomQuery(String text);

  List<Product> searchByExample(Object example);

}
