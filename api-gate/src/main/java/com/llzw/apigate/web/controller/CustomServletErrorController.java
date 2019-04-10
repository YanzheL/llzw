package com.llzw.apigate.web.controller;

import com.llzw.apigate.web.util.StandardRestResponse;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
public class CustomServletErrorController extends BasicErrorController {

  public CustomServletErrorController(ErrorAttributes errorAttributes) {
    super(errorAttributes, new ErrorProperties());
  }

  @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity errorJson(HttpServletRequest request) {
    Map<String, Object> attrs = getErrorAttributes(request,
        isIncludeStackTrace(request, MediaType.ALL));
    return StandardRestResponse.errorResponseEntity(
        new Exception((String) attrs.get("message")),
        HttpStatus.BAD_REQUEST
    );
  }
}

