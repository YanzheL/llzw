package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductSearchableRepository {

  List<Product> searchByNameOrIntroductionWithCustomQuery(String text, Pageable pageable);

  List<Product> searchByExample(Object example, Pageable pageable);

}
