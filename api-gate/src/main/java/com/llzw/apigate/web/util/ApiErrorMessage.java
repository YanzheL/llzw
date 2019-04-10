package com.llzw.apigate.web.util;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorMessage implements Serializable {

  private String type;
  private String message;
}
