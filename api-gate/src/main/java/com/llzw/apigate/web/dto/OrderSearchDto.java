package com.llzw.apigate.web.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderSearchDto {

  String customerId;

  Long addressId;

  Long stockId;

  @Size(max = 30, message = "Length cannot exceed 30")
  String trackingId;
}
