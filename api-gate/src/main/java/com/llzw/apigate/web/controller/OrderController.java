package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.OrderService;
import com.llzw.apigate.web.dto.OrderCreateDto;
import com.llzw.apigate.web.dto.OrderSearchDto;
import com.llzw.apigate.web.util.RestResponseFactory;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@BasePathAwareController
@RequestMapping(value = "/orders")
public class OrderController {

  @Setter(onMethod_ = @Autowired)
  private OrderService orderService;

  @PreAuthorize("hasAuthority('OP_READ_ORDER')")
  @GetMapping
  public ResponseEntity searchOrders(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      OrderSearchDto searchDto) throws RestApiException {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseFactory.success(
        orderService.search(searchDto, currentUser, pageRequest)
    );
  }

  @PreAuthorize("hasAuthority('OP_READ_ORDER')")
  @GetMapping(value = "/{id}")
  public ResponseEntity getOrder(@PathVariable(value = "id") Long id) {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Optional<Order> result = orderService.get(id);
    if (!result.isPresent()) {
      return RestResponseFactory.error(
          new RestEntityNotFoundException(),
          HttpStatus.NOT_FOUND
      );
    }
    Order order = result.get();
    if (!order.belongsToUser(currentUser)) {
      return RestResponseFactory.error(
          new RestAccessDeniedException("Current user does not have access to this order"),
          HttpStatus.FORBIDDEN);
    }
    return RestResponseFactory.success(order);
  }

  @PreAuthorize("hasAuthority('OP_CREATE_ORDER')")
  @PostMapping
  @Transactional
  public ResponseEntity createOrder(@Valid OrderCreateDto orderCreateDto) throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseFactory.success(
        orderService.create(
            currentUser,
            orderCreateDto.getProductId(),
            orderCreateDto.getQuantity(),
            orderCreateDto.getAddressId()
        ),
        HttpStatus.CREATED
    );
  }
}
