package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.ProductCreateDto;
import java.util.List;
import java.util.Optional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

  @Override
  public Product create(ProductCreateDto dto, User seller) throws RestApiException {
    Product product = new Product();
    product.setSeller(seller);
    product.setName(dto.getName());
    product.setIntroduction(dto.getIntroduction());
    product.setPrice(dto.getPrice());
    product.setCaId(dto.getCaId());
    product.setValid(true);
    return productRepository.save(product);
  }

  @Override
  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  @Override
  public List<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable).getContent();
  }
}
