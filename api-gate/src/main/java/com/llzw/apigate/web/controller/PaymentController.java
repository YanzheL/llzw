package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.PaymentService;
import com.llzw.apigate.service.error.ApiServiceException;
import com.llzw.apigate.web.dto.PaymentCreateDto;
import com.llzw.apigate.web.util.StandardRestResponse;
import java.util.Map;
import javax.validation.Valid;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@BasePathAwareController
@RequestMapping(value = "/payments")
public class PaymentController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Setter(onMethod_ = @Autowired)
  private PaymentService paymentService;

  /**
   * Create a payment for an order, and generate the corresponding order string. Clients can use
   * this order string to perform actual payment action on vendor's website.
   *
   * @return The created payment object with a new order string from vendor.
   */
  @PreAuthorize("hasRole('CUSTOMER')")
  @PostMapping
  public ResponseEntity create(@Valid PaymentCreateDto paymentCreateDto)
      throws ApiServiceException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Payment payment = paymentService
        .create(
            currentUser,
            paymentCreateDto.getOrderId(),
            paymentCreateDto.getSubject(),
            paymentCreateDto.getDescription()
        );
    return StandardRestResponse.getResponseEntity(payment, true, HttpStatus.CREATED);
  }

  /**
   * Re-obtain order string for a specific payment. No authentication required here.
   *
   * @param paymentId Id of target payment. The target payment must not be confirmed.
   * @return New order string from vendor. Clients can use this order string to perform actual
   * payment action on vendor's website.
   */
  @GetMapping("/retry")
  public ResponseEntity retry(@RequestParam(value = "paymentId") Long paymentId)
      throws ApiServiceException {
    Payment payment = paymentService.retry(paymentId);
    return StandardRestResponse
        .getResponseEntity(payment.getOrderString(), true, HttpStatus.CREATED);
  }

  /**
   * Verify async payment confirmation (notification) from vendor. If success, the target payment
   * will be marked {@code confirmed}, and the target order will be marked {@code paid}.
   *
   * @param allRequestParams All params posted from vendor.
   */
  @PostMapping("/verify")
  public void verify(@RequestParam Map<String, String> allRequestParams) {
    try {
      paymentService.verify(allRequestParams);
    } catch (ApiServiceException e) {
      LOGGER.warn(e.getErrorCode());
    }
  }
}