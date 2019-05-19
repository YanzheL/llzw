package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.StockService;
import com.llzw.apigate.web.dto.StockCreateDto;
import com.llzw.apigate.web.dto.StockSearchDto;
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
  private StockService stockService;

  /**
   * Create a new stock
   */
  @PreAuthorize("hasAnyRole('SELLER')")
  @PostMapping
  public ResponseEntity createStock(@Valid @RequestBody StockCreateDto stockCreateDto)
      throws RestApiException {
    return RestResponseEntityFactory.success(
        stockService.create(
            stockCreateDto.getProductId(),
            stockCreateDto.getProducedAt(),
            stockCreateDto.getShelfLife(),
            stockCreateDto.getTotalQuantity(),
            stockCreateDto.getTrackingId(),
            stockCreateDto.getCarrierName()
        ), HttpStatus.CREATED
    );
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
    return RestResponseEntityFactory.success(
        stockService.search(currentUser, dto, pageRequest)
    );
  }

  /**
   * Get a specific stock
   */
  @PreAuthorize("hasAnyRole('SELLER')")
  @GetMapping(value = "/{id:\\d+}")
  public ResponseEntity getStock(@PathVariable(value = "id") Long id) throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(stockService.findById(currentUser, id));
  }
}
