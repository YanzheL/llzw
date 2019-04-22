package com.llzw.apigate.web.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class StockCreateDto {

  @NotNull
  //@Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected Long productId;

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date producedAt;

  @NotNull
  protected Integer shelfLife;

  @NotNull
  @Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected Integer totalQuantity;

  @NotNull
  @Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected String currentQuantity;

  @NotNull
  @Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected String trackingId;

  @NotNull
  @Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected String carrierName;
}
