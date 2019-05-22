package com.llzw.apigate.web.dto;

import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class OrderPatchDto {

  protected String trackingId;

  protected String carrierName;

  protected String remark;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date shippingTime;
}
