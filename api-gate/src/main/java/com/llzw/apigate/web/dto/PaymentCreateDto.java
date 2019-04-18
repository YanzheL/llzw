package com.llzw.apigate.web.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCreateDto {

  @NotNull
  protected Long orderId;

  @NotNull
  protected String subject;

  protected String description;
}
