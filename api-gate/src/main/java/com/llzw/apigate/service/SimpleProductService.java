package com.llzw.apigate.service;

import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.entity.Product;
import java.util.Collection;
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
  public boolean updateValid(
      Long id, Collection<String> msgs) {
    return applyToUser(
        id,
        product -> {
          product.setValid(false);
          return true;
        },
        msgs);
  }

  /*
  * make sure whether specific product exists
  * */
  private boolean applyToUser(Long id, Predicate<Product> product, Collection<String> msgs) {
    Optional<Product> productOptional = productRepository.findById(id);
    if (productOptional.isPresent()) {
      Product found = productOptional.get();
      boolean success = product.test(found);
      if (success) {
        productRepository.save(found);
      }
      return success;
    } else {
      msgs.add("Product doesn't exist");
      return false;
    }
  }
}
