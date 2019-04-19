package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.customquery.SearchCriterion;
import com.llzw.apigate.persistence.dao.customquery.SearchCriterionSpecificationFactory;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.StockCreateDto;
import com.llzw.apigate.web.dto.StockSearchDto;
import com.llzw.apigate.web.util.StandardRestResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.RepositoryRestController;
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

@RepositoryRestController
@RequestMapping(value = "/stocks")
public class StockController {

  @Setter(onMethod_ = @Autowired)
  private StockRepository stockRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  /*
   * create a new stock
   * */
  @PreAuthorize("hasAnyRole('SELLER')")
  @PostMapping(value = "")
  @Transactional          // transaction management
  public ResponseEntity createStock(@Valid StockCreateDto stockCreateDto) {

    Optional<Product> productOptional = productRepository.findById(stockCreateDto.getProductId().getId());
    if (!productOptional.isPresent()) {
      return StandardRestResponse.getResponseEntity(
          "Cannot find specified product", false, HttpStatus.NOT_FOUND);
    }

    Stock stock = new Stock();
    stock.setProductId(productOptional.get());
    stock.setProducedAt(stockCreateDto.getProducedAt());
    stock.setShelfLife(stockCreateDto.getShelfLife());
    stock.setTotalQuantity(stockCreateDto.getTotalQuantity());
    stock.setTrackingId(stockCreateDto.getTrackingId());
    stock.setCarrierName(stockCreateDto.getCarrierName());
    Stock saveOpResult = stockRepository.save(stock);
    return StandardRestResponse.getResponseEntity(saveOpResult, true, HttpStatus.CREATED);
  }

  /*
   * get stock by given parameters
   * */
  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @GetMapping(value = "")
  public ResponseEntity searchStock(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      StockSearchDto searchDto)throws IllegalAccessException  {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    //Optional<Product> productOptional = productRepository.findById(stockCreateDto.getProductId());
    //get all stock by given parameters
    List<Stock> allMatchingProducts =
        stockRepository
            .findAll(SearchCriterionSpecificationFactory.fromExample(searchDto), pageRequest)
            .getContent();
    // Results may contain other product's order, so we should filter them out.
    List<Stock> res =
        allMatchingProducts.stream()
            .filter(o -> o.belongsToUser(currentUser))
            .collect(Collectors.toList());
    return res.isEmpty()
        ? StandardRestResponse.getResponseEntity(null, false, HttpStatus.NOT_FOUND)
        : StandardRestResponse.getResponseEntity(res, true);
  }

  /*
  * get specific stock
  * */
  @PreAuthorize("hasAnyRole('SELLER')")
  @GetMapping(value = "/{id}")
  public ResponseEntity getStock(@PathVariable(value = "id") Long id) {
    Optional<Stock> res = stockRepository.findById(id);
    if (!res.isPresent()) {
      return StandardRestResponse.getResponseEntity(null, false, HttpStatus.NOT_FOUND);
    }
    Stock stock = res.get();
    return StandardRestResponse.getResponseEntity(stock, true);
  }

  // TODO: Can we generalize this?
  private Specification<Stock> findByExample(
      Long productId, Long shelfLife, Long trackingId, String carrierName,Boolean valid) {
    List<SearchCriterion> criteria = new ArrayList<>();
    if (productId != null) {
      criteria.add(new SearchCriterion("productId", "=", productId));
    }
    if (shelfLife != null) {
      criteria.add(new SearchCriterion("shelfLife", "=", shelfLife));
    }
    if (trackingId != null) {
      criteria.add(new SearchCriterion("trackingId", "=", trackingId));
    }
    if (carrierName != null) {
      criteria.add(new SearchCriterion("carrierName", "=", carrierName));
    }
    //valid=true: the stock is valid
    if (valid != null) {
      criteria.add(new SearchCriterion("valid", "=", valid));
    }


    return SearchCriterionSpecificationFactory.and(criteria);
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
