package com.llzw.apigate.web.error;

import com.llzw.apigate.message.RestResponseFactory;
import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.message.error.TradeNotFoundPaymentVendorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler
  protected ResponseEntity<Object> handleAny(Exception ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return handleAny(ex, status);
  }

  @ExceptionHandler({AccessDeniedException.class})
  protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
    return handleRestApiException(
        new RestAccessDeniedException(ex.getMessage())
    );
  }

  protected ResponseEntity<Object> handleAny(Exception ex, HttpStatus status) {
    RestApiException apiException = new RestApiException(ex.getMessage());
    return RestResponseFactory.error(apiException, status);
  }

  @ExceptionHandler({RestApiException.class})
  protected ResponseEntity<Object> handleRestApiException(RestApiException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    if (ex instanceof RestDependentEntityNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex instanceof RestEntityNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex instanceof TradeNotFoundPaymentVendorException) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex instanceof RestAccessDeniedException) {
      status = HttpStatus.FORBIDDEN;
    }
    return RestResponseFactory.error(ex, status);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      @Nullable Object body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleAny(ex, status);
  }
}
