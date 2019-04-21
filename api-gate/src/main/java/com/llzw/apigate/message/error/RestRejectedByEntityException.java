package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestRejectedByEntityException extends RestApiException {

  private final static String type = "REJECTED_BY_ENTITY";

  public RestRejectedByEntityException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
