package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository
    extends
    PagingAndSortingRepository<Product, Long>,
    JpaSpecificationExecutor<Product>,
    ProductSearchableRepository {

  List<Product> findAllBySellerUsername(String username);

  Optional<Product> findByIdAndSeller(Long id, User user);

  List<Product> findAllByValid(boolean valid);
}
