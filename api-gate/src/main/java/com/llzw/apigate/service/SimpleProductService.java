package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.entity.Product;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SimpleProductService implements ProductService {

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Override
  public boolean updateValid(Long id) throws RestApiException {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Product <%s> do not exist", id)));
    product.setValid(false);
    productRepository.save(product);
    return true;
  }
}
