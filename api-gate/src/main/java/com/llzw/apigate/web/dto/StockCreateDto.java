package com.llzw.apigate.web.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class StockCreateDto {

  @NotNull
  protected Long productId;

  @PastOrPresent(message = "Date should be past or present")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date producedAt;

  @Positive(message = "shelfLife should be positive")
  protected Integer shelfLife;

  @Positive(message = "Quantity should be positive")
  protected Integer totalQuantity;

  @Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected String trackingId;

  @Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected String carrierName;
}
