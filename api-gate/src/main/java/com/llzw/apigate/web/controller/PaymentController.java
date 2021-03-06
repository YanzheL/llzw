package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.PaymentService;
import com.llzw.apigate.web.dto.PaymentCreateDto;
import java.util.Map;
import javax.validation.Valid;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// @RepositoryRestController is fucking not working as expected, referenced issue: https://jira.spring.io/browse/DATAREST-972
// @RestController creates duplicate endpoints with and without base-path. The same issue as described above.
@Validated
@Controller
@ResponseBody
@RequestMapping(value = "${spring.data.rest.base-path}/payments")
@Transactional
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
  public ResponseEntity create(
      @Valid @RequestBody PaymentCreateDto paymentCreateDto,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    Payment payment = paymentService
        .create(
            currentUser,
            paymentCreateDto.getOrderId(),
            paymentCreateDto.getSubject(),
            paymentCreateDto.getDescription()
        );
    return RestResponseEntityFactory.success(payment, HttpStatus.CREATED);
  }

  @PreAuthorize("hasAnyRole('CUSTOMER','SELLER')")
  @GetMapping("/{id:\\d+}")
  public ResponseEntity get(
      @PathVariable(value = "id") Long paymentId,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    return RestResponseEntityFactory
        .success(paymentService.findById(currentUser, paymentId));
  }

  @PreAuthorize("hasAnyRole('CUSTOMER','SELLER')")
  @GetMapping
  public ResponseEntity getByOrderId(
      @RequestParam(value = "orderId") String orderId,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    return RestResponseEntityFactory
        .success(paymentService.findByOrderId(currentUser, orderId));
  }

  /**
   * Re-obtain order string for a specific payment. No authentication required here.
   *
   * @param paymentId Id of target payment. The target payment must not be confirmed.
   * @return New order string from vendor. Clients can use this order string to perform actual
   * payment action on vendor's website.
   */
  @GetMapping("/retry/{id:\\d+}")
  public ResponseEntity retry(@PathVariable(value = "id") Long paymentId) throws RestApiException {
    Payment payment = paymentService.retry(paymentId);
    return RestResponseEntityFactory
        .success(payment.getOrderString());
  }

  /**
   * Verify async payment confirmation (notification) from vendor. If success, the target payment
   * will be marked {@code confirmed}, and the target order will be marked {@code paid}.
   *
   * @param allRequestParams All params posted from vendor.
   */
  @RequestMapping(value = "/verify", method = {RequestMethod.POST, RequestMethod.GET})
  public void verify(@RequestParam Map<String, String> allRequestParams) {
    try {
      paymentService.verifyFromVendor(allRequestParams);
    } catch (RestApiException e) {
      LOGGER.warn(e.getType());
    }
  }

  @GetMapping("/verify/{id:\\d+}")
  public ResponseEntity verify(@PathVariable(value = "id") Long id) throws RestApiException {
    return RestResponseEntityFactory.success(paymentService.verify(id));
  }
}
