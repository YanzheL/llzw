package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.service.ProductService;
import com.llzw.apigate.web.dto.ProductCreateDto;
import com.llzw.apigate.web.util.StandardRestResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestController
@RequestMapping(value = "/products")
public class ProductController {

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductService productService;

  /*
   * create new product
   * */
  @PreAuthorize("hasRole('SELLER')")
  @PostMapping
  @Transactional          // transaction management
  public ResponseEntity createProduct(@Valid ProductCreateDto productCreateDto) {
    //将dto中商品的各种信息存入product中
    Product product = new Product();
    product.setName(productCreateDto.getProductName());
    product.setIntroduction(productCreateDto.getIntroduction());
    product.setPrice(productCreateDto.getPrice());
    product.setCaId(productCreateDto.getCerId());
    Product saveOpResult = productRepository.save(product);
    return StandardRestResponse.getResponseEntity(saveOpResult, true, HttpStatus.CREATED);
  }

  /*
   * scan all products
   * */
  @GetMapping
  @Transactional          // transaction management
  public ResponseEntity scanProduct(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @RequestParam(value = "valid", required = false, defaultValue = "True") boolean valid) {

    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    List<Product> allMatchingProducts = productRepository.findAll(pageRequest).getContent();
    return StandardRestResponse.getResponseEntity(allMatchingProducts);
  }

  /*
   * search product by id
   * */
  @GetMapping(value = "/{id}")
  @Transactional          // transaction management
  public ResponseEntity findProductById(@PathVariable(value = "id") Long id) {
    Optional<Product> res = productRepository.findById(id);
    return res.isPresent()
        ? StandardRestResponse.getResponseEntity(res)
        : StandardRestResponse.getResponseEntity(null, false, HttpStatus.NOT_FOUND);
  }

  /*
   * invalidate a specific product
   * */
  @DeleteMapping(value = "/{id}")
  @Transactional          // transaction management
  public ResponseEntity invalidateProductById(@PathVariable(value = "id") Long id) {

//    LOGGER.debug("Verifying user account with information: {}", updatePasswordDto);
//    Product currentUser =
//        ((Product) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Collection<String> msgs = new ArrayList<>();
    return StandardRestResponse.getResponseEntity(
        msgs,
        productService.updateValid(
            id,
            msgs));
  }


}
