package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// @RepositoryRestController is fucking not working as expected, referenced issue: https://jira.spring.io/browse/DATAREST-972
// @RestController creates duplicate endpoints with and without base-path. The same issue as described above.
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class CustomServletErrorController extends AbstractErrorController {

  public CustomServletErrorController(ErrorAttributes errorAttributes) {
    super(errorAttributes);
  }

  @RequestMapping
  public ResponseEntity handleError(HttpServletRequest request) {
    return errorJson(request);
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

  @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity errorJson(HttpServletRequest request) {
    Map<String, Object> attrs = getErrorAttributes(request, false);
    HttpStatus status = getStatus(request);
    return RestResponseEntityFactory.error(
        new RestApiException((String) attrs.get("message")),
        status
    );
  }
}

