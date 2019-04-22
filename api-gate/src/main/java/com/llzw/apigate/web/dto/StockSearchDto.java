package com.llzw.apigate.web.dto;

import lombok.Data;

@Data
public class StockSearchDto {

  Long productId;

  Integer shelfLife;

  String trackingId;

  String carrierName;

  Boolean valid;
}
