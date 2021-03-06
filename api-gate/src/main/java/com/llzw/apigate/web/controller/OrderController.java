package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.OrderService;
import com.llzw.apigate.web.dto.OrderCreateDto;
import com.llzw.apigate.web.dto.OrderSearchDto;
import com.llzw.apigate.web.dto.OrderShipDto;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping(value = "${spring.data.rest.base-path}/orders")
@Transactional
public class OrderController {

  @Setter(onMethod_ = @Autowired)
  private OrderService orderService;

  @PreAuthorize("hasAuthority('OP_READ_ORDER')")
  @GetMapping
  public ResponseEntity search(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @Valid OrderSearchDto searchDto,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    return RestResponseEntityFactory.success(
        orderService.search(searchDto, currentUser, pageRequest)
    );
  }

  @PreAuthorize("hasAuthority('OP_READ_ORDER')")
  @GetMapping(value = "/{id:[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}}")
  public ResponseEntity get(@PathVariable(value = "id") String id,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    return RestResponseEntityFactory.success(orderService.get(id, currentUser));
  }

  @PreAuthorize("hasAuthority('OP_CREATE_ORDER')")
  @PostMapping
  public ResponseEntity create(
      @Valid @RequestBody OrderCreateDto orderCreateDto,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    return RestResponseEntityFactory.success(
        orderService.create(
            currentUser,
            orderCreateDto.getProductId(),
            orderCreateDto.getQuantity(),
            orderCreateDto.getAddressId(),
            orderCreateDto.getRemark()
        ),
        HttpStatus.CREATED
    );
  }

  @PreAuthorize("hasAuthority('OP_DELETE_ORDER')")
  @DeleteMapping(value = "/{id:[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}}")
  public ResponseEntity cancel(
      @PathVariable(value = "id") String id,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    return RestResponseEntityFactory.success(
        orderService.cancel(id, currentUser)
    );
  }

  @PreAuthorize("hasRole('CUSTOMER')")
  @PatchMapping(value = "/{id:[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}}/DELIVERY_CONFIRM")
  public ResponseEntity patch(
      @PathVariable(value = "id") String id,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    return RestResponseEntityFactory.success(orderService.deliveryConfirm(id, currentUser));
  }

  @PreAuthorize("hasRole('SELLER')")
  @PatchMapping(value = "/{id:[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}}/SHIP")
  public ResponseEntity patch(
      @PathVariable(value = "id") String id,
      @Valid @RequestBody OrderShipDto orderShipDto,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    return RestResponseEntityFactory
        .success(orderService.patch(id, orderShipDto, currentUser));
  }
}
