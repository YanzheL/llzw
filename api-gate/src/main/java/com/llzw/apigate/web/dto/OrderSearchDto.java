package com.llzw.apigate.web.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderSearchDto {

  protected String customerId;

  protected Long stockId;

  @Size(max = 30, message = "Length cannot exceed 30")
  protected String trackingId;

  protected boolean valid = true;
}
