package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestUnsupportedOperationException;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.OrderService;
import com.llzw.apigate.web.dto.OrderCreateDto;
import com.llzw.apigate.web.dto.OrderSearchDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@BasePathAwareController
@RequestMapping(value = "/orders")
public class OrderController {

  @Setter(onMethod_ = @Autowired)
  private OrderService orderService;

  @PreAuthorize("hasAuthority('OP_READ_ORDER')")
  @GetMapping
  public ResponseEntity search(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      OrderSearchDto searchDto) throws RestApiException {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(
        orderService.search(searchDto, currentUser, pageRequest)
    );
  }

  @PreAuthorize("hasAuthority('OP_READ_ORDER')")
  @GetMapping(value = "/{id:\\d+}")
  public ResponseEntity get(@PathVariable(value = "id") Long id)
      throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(orderService.get(id, currentUser));
  }

  @PreAuthorize("hasAuthority('OP_CREATE_ORDER')")
  @PostMapping
  @Transactional
  public ResponseEntity create(@Valid OrderCreateDto orderCreateDto) throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(
        orderService.create(
            currentUser,
            orderCreateDto.getProductId(),
            orderCreateDto.getQuantity(),
            orderCreateDto.getAddressId()
        ),
        HttpStatus.CREATED
    );
  }

  @PreAuthorize("hasAuthority('OP_DELETE_ORDER')")
  @DeleteMapping(value = "/{id:\\d+}")
  public ResponseEntity cancel(@PathVariable(value = "id") Long id) throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(
        orderService.cancel(id, currentUser)
    );
  }

  @PreAuthorize("hasRole('CUSTOMER')")
  @PatchMapping(value = "/{id:\\d+}")
  public ResponseEntity deliveryConfirm(
      @PathVariable(value = "id") Long id,
      @RequestParam(value = "action") String action
  ) throws RestApiException {
    if (!action.equals("DELIVERY_CONFIRM")) {
      throw new RestUnsupportedOperationException(String.format("Action <%s> unsupported", action));
    }
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(
        orderService.deliveryConfirm(id, currentUser)
    );
  }
}
