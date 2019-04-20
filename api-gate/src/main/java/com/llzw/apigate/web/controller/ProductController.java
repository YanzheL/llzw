package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.ProductService;
import com.llzw.apigate.web.dto.ProductCreateDto;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@BasePathAwareController
@RequestMapping(value = "/products")
public class ProductController {

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductService productService;

  /**
   * Create new product
   */
  @PreAuthorize("hasRole('SELLER')")
  @PostMapping
  @Transactional          // transaction management
  public ResponseEntity create(@Valid ProductCreateDto productCreateDto) {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    //将dto中商品的各种信息存入product中
    Product product = new Product();
    product.setSeller(currentUser);
    product.setName(productCreateDto.getName());
    product.setIntroduction(productCreateDto.getIntroduction());
    product.setPrice(productCreateDto.getPrice());
    product.setCaId(productCreateDto.getCaId());
    Product saveOpResult = productRepository.save(product);
    return RestResponseEntityFactory.success(saveOpResult, HttpStatus.CREATED);
  }

  /**
   * Search all products
   */
  @GetMapping
  public ResponseEntity search(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @RequestParam(value = "valid", required = false, defaultValue = "True") boolean valid) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    List<Product> allMatchingProducts = productRepository.findAll(pageRequest).getContent();
    return RestResponseEntityFactory.success(allMatchingProducts);
  }

  /**
   * Get product by id
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity get(@PathVariable(value = "id") Long id) {
    Optional<Product> res = productRepository.findById(id);
    return res.isPresent()
        ? RestResponseEntityFactory.success(res)
        : RestResponseEntityFactory.error(new RestEntityNotFoundException());
  }

  /**
   * Invalidate a specific product
   */
  @PreAuthorize("hasRole('SELLER')")
  @DeleteMapping(value = "/{id}")
  @Transactional
  public ResponseEntity invalidate(@PathVariable(value = "id") Long id) throws RestApiException {
    return RestResponseEntityFactory.success(productService.updateValid(id));
  }
}
