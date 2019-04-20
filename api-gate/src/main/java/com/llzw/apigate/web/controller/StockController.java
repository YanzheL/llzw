package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.message.error.RestInvalidParameterException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.customquery.SearchCriterionSpecificationFactory;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.StockCreateDto;
import com.llzw.apigate.web.dto.StockSearchDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@BasePathAwareController
@RequestMapping(value = "/stocks")
public class StockController {

  @Setter(onMethod_ = @Autowired)
  private StockRepository stockRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  /**
   * Create a new stock
   */
  @PreAuthorize("hasAnyRole('SELLER')")
  @PostMapping(value = "")
  @Transactional          // transaction management
  public ResponseEntity createStock(@Valid StockCreateDto stockCreateDto) throws RestApiException {
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
    Stock saveOpResult = stockRepository.save(stock);
    return RestResponseEntityFactory.success(saveOpResult, HttpStatus.CREATED);
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
      List<Stock> res =
          stockRepository
              .findAll(SearchCriterionSpecificationFactory.fromExample(dto), pageRequest)
              .getContent().stream()
              .filter(o -> o.belongsToSeller(currentUser))
              .collect(Collectors.toList());
      return RestResponseEntityFactory.success(res);
    } catch (IllegalAccessException e) {
      throw new RestInvalidParameterException(e.getMessage());
    }
  }

  /**
   * Get a specific stock
   */
  @PreAuthorize("hasAnyRole('SELLER')")
  @GetMapping(value = "/{id}")
  public ResponseEntity getStock(@PathVariable(value = "id") Long id) throws RestApiException {
    return RestResponseEntityFactory.success(
        stockRepository.findById(id).orElseThrow(() -> new RestEntityNotFoundException(
            String.format("Stock <%d> does not exist", id)
        ))
    );
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
