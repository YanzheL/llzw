package com.llzw.apigate.web.dto;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class OrderShipDto {

  @NotBlank
  protected String trackingId;

  @NotBlank
  protected String carrierName;

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date shippingTime;
}
