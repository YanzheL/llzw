package com.llzw.apigate.message;

import org.springframework.http.HttpStatus;

public interface HttpStatusAdvisor {

  /**
   * Provide default suggestion
   *
   * @return BAD_REQUEST
   */
  default HttpStatus suggestHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
