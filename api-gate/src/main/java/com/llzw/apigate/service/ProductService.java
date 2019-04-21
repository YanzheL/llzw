package com.llzw.apigate.service;


import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.ProductCreateDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  boolean updateValid(Long id, User seller) throws RestApiException;

  Product create(ProductCreateDto dto, User seller) throws RestApiException;

  Optional<Product> findById(Long id);

  List<Product> findAll(Pageable pageable);
}