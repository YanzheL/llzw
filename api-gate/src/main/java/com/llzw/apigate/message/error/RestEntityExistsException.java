package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestEntityExistsException extends RestApiException {

  private final static String type = "ENTITY_ALREADY_EXISTS";

  public RestEntityExistsException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
