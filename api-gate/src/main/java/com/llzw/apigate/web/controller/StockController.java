package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestInvalidParameterException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.customquery.JpaSearchSpecificationFactory;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.StockCreateDto;
import com.llzw.apigate.web.dto.StockSearchDto;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(value = "${spring.data.rest.base-path}/stocks")
@Transactional
public class StockController {

  @Setter(onMethod_ = @Autowired)
  private StockRepository stockRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  /**
   * Create a new stock
   */
  @PreAuthorize("hasAnyRole('SELLER')")
  @PostMapping
  public ResponseEntity createStock(@Valid @RequestBody StockCreateDto stockCreateDto)
      throws RestApiException {
    Optional<Product> productOptional = productRepository.findById(stockCreateDto.getProductId());
    if (!productOptional.isPresent()) {
      throw new RestDependentEntityNotFoundException(
          String.format("Product <%s> do not exist", stockCreateDto.getProductId()));
    }
    Stock stock = new Stock();
    stock.setProduct(productOptional.get());
    stock.setProducedAt(stockCreateDto.getProducedAt());
    stock.setShelfLife(stockCreateDto.getShelfLife());
    stock.setTotalQuantity(stockCreateDto.getTotalQuantity());
    stock.setTrackingId(stockCreateDto.getTrackingId());
    stock.setCarrierName(stockCreateDto.getCarrierName());
    stock.setValid(true);
    return RestResponseEntityFactory.success(stockRepository.save(stock), HttpStatus.CREATED);
  }

  /**
   * Search stocks by given parameters
   */
  @PreAuthorize("hasRole('SELLER')")
  @GetMapping
  public ResponseEntity searchStock(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      StockSearchDto dto) throws RestApiException {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    try {
      // Results may contain other user's stock, so we should filter them out.
      return RestResponseEntityFactory.success(stockRepository
          .findAll(JpaSearchSpecificationFactory.fromExample(dto), pageRequest)
          .getContent().stream()
          .filter(o -> o.belongsToSeller(currentUser))
          .collect(Collectors.toList()));
    } catch (IllegalAccessException e) {
      throw new RestInvalidParameterException(e.getMessage());
    }
  }

  /**
   * Get a specific stock
   */
  @PreAuthorize("hasAnyRole('SELLER')")
  @GetMapping(value = "/{id:\\d+}")
  public ResponseEntity getStock(@PathVariable(value = "id") Long id) throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Stock stock = stockRepository.findById(id)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Stock <%s> does not exist", id)));
    if (!stock.belongsToSeller(currentUser)) {
      throw new RestAccessDeniedException("You do not have access to this entity");
    }
    return RestResponseEntityFactory.success(stock);
  }

  /*
   * invalidate a specific product
   * */
//  @DeleteMapping(value = "/{id}")
//  @Transactional          // transaction management
//  public ResponseEntity invalidateStockById(@PathVariable(value = "id") Long id) {
//
////    LOGGER.debug("Verifying user account with information: {}", updatePasswordDto);
////    Product currentUser =
////        ((Product) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//    Collection<String> msgs = new ArrayList<>();
//    return StandardRestResponse.getResponseEntity(
//        msgs,
//        stockService.updateValid(
//            id,
//            msgs));
//  }
}
