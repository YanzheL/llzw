package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.ProductService;
import com.llzw.apigate.web.dto.ProductCreateDto;
import com.llzw.apigate.web.dto.ProductSearchDto;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// @RepositoryRestController is fucking not working as expected, referenced issue: https://jira.spring.io/browse/DATAREST-972
// @RestController creates duplicate endpoints with and without base-path. The same issue as described above.
@Validated
@Controller
@ResponseBody
@RequestMapping(value = "${spring.data.rest.base-path}/products")
@Transactional          // transaction management
public class ProductController {

  @Setter(onMethod_ = @Autowired)
  private ProductService productService;

  /**
   * Create new product
   */
  @PreAuthorize("hasRole('SELLER')")
  @PostMapping
  public ResponseEntity create(@Valid @RequestBody ProductCreateDto productCreateDto)
      throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory
        .success(productService.create(productCreateDto, currentUser), HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('SELLER')")
  @PatchMapping(value = "/{id:\\d+}")
  public ResponseEntity update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody ProductCreateDto productCreateDto
  ) throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory
        .success(productService.update(productCreateDto, id, currentUser));
  }

  /**
   * Search all products
   */
  @GetMapping
  public ResponseEntity search(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @Valid ProductSearchDto productSearchDto) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    return RestResponseEntityFactory.success(productService.search(pageRequest, productSearchDto));
  }

  /**
   * Get product by id
   */
  @GetMapping(value = "/{id:\\d+}")
  public ResponseEntity get(@PathVariable(value = "id") Long id) throws RestApiException {
    return RestResponseEntityFactory.success(
        productService.findById(id).orElseThrow(() -> new RestEntityNotFoundException(
            String.format("Product <%d> does not exist", id)
        ))
    );
  }

  /**
   * Invalidate a specific product
   */
  @PreAuthorize("hasRole('SELLER')")
  @DeleteMapping(value = "/{id:\\d+}")
  public ResponseEntity invalidate(@PathVariable(value = "id") Long id) throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(productService.invalidate(id, currentUser));
  }
}
