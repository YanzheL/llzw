package com.llzw.apigate.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderDto {

  @NotNull
  protected Long productId;

  @Positive
  protected int quantity;

  @NotNull
  protected Long addressId;
}
