package com.llzw.apigate.spring;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestApiExceptionWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    return RestResponseEntityFactory.error(RestApiExceptionWrapper.wrap(ex));
  }

  protected ResponseEntity<Object> handleAny(Exception ex, HttpStatus status) {
    return RestResponseEntityFactory.error(
        RestApiExceptionWrapper.wrap(ex),
        status
    );
  }

  @ExceptionHandler({RestApiException.class})
  protected ResponseEntity<Object> handleRestApiException(RestApiException ex) {
    return RestResponseEntityFactory.error(ex);
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
