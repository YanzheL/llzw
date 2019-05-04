package com.llzw.apigate.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentCreateDto {

  @NotNull
  protected Long orderId;

  @Size(min = 5, max = 100, message = "Length must between 5 and 100")
  protected String subject;

  protected String description;
}
