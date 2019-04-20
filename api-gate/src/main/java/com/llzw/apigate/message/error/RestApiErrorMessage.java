package com.llzw.apigate.message.error;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.llzw.apigate.message.HttpStatusAdvisor;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public interface RestApiErrorMessage extends HttpStatusAdvisor {

  @JsonGetter("type")
  String getType();

  @JsonGetter("message")
  String getMessage();
}
