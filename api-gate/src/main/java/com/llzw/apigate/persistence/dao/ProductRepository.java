package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository
    extends
    PagingAndSortingRepository<Product, Long>,
    JpaSpecificationExecutor<Product>,
    ProductSearchableRepository {

  List<Product> findAllBySellerUsername(String username);
}
