package com.llzw.apigate.web.dto;

import lombok.Data;

@Data
public class OrderSearchDto {

  String customerId;

  Long addressId;

  Long stockId;

  String trackingId;
}
