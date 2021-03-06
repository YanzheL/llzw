package com.llzw.apigate.service;


import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.ProductCreateDto;
import com.llzw.apigate.web.dto.ProductSearchDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  boolean invalidate(Long id, User seller) throws RestApiException;

  Product create(ProductCreateDto dto, User seller) throws RestApiException;

  Product update(ProductCreateDto dto, Long id, User seller) throws RestApiException;

  Optional<Product> findById(Long id);

  List<Product> search(Pageable pageable, ProductSearchDto example) throws RestApiException;

  Product save(Product product);
}