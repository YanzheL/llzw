package com.llzw.apigate.web.dto;

import lombok.Data;

@Data
public class StockSearchDto {

  Integer productId;

  Integer shelfLife;

  String trackingId;

  String carrierName;

  Boolean valid;
}
