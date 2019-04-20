package com.llzw.apigate.web.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class StockCreateDto {
  @NotNull
  //@Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected Long productId;

  @NotNull
  protected Date createdAt;

  @NotNull
  protected Date updatedAt;

  @NotNull
  protected Date producedAt;

  @NotNull
  protected Date inboundedAt;

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

  @NotNull
  protected Boolean valid;
}
