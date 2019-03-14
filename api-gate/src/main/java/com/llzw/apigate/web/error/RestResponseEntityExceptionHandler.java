package com.llzw.apigate.web.error;

import com.llzw.apigate.web.util.StandardRestResponse;
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

  //  @Autowired private MessageSource messages;

  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler
  protected ResponseEntity<Object> handle(Exception ex) {
    return handle(ex, HttpStatus.BAD_REQUEST);
  }

  protected ResponseEntity<Object> handle(Exception ex, HttpStatus status) {
    return StandardRestResponse.getResponseEntity(ex.getMessage(), false, status);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      @Nullable Object body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handle(ex, status);
  }
}
