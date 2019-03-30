package com.llzw.apigate.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class OrderDto {

  @NotNull protected Long product_id;

  @Positive protected int quantity;

  @NotNull protected Long address_id;
}
