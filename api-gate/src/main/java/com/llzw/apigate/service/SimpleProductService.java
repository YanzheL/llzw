package com.llzw.apigate.service;

import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.service.error.RequestedDependentObjectNotFoundException;
import com.llzw.apigate.service.error.RestApiException;
import java.util.Optional;
import java.util.function.Predicate;
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
    return applyToUser(
        id,
        product -> {
          product.setValid(false);
          return true;
        });
  }

  /*
   * make sure whether specific product exists
   * */
  private boolean applyToUser(Long id, Predicate<Product> product) throws RestApiException {
    Optional<Product> productOptional = productRepository.findById(id);
    if (!productOptional.isPresent()) {
      throw new RequestedDependentObjectNotFoundException(
          String.format("Product <%s> do not exist", id));
    }
    Product found = productOptional.get();
    boolean success = product.test(found);
    if (success) {
      productRepository.save(found);
    }
    return success;
  }
}
