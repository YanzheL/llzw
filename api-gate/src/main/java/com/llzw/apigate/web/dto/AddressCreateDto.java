package com.llzw.apigate.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressCreateDto {

  @NotNull
  protected Long ownerId;

  @Size(min = 2, max = 30, message = "Length must between 2 and 30")
  protected String province;

  @Size(min = 2, max = 30, message = "Length must between 2 and 30")
  protected String city;

  @Size(min = 2, max = 30, message = "Length must between 2 and 30")
  protected String district;

  @Size(min = 2, max = 100, message = "Length must between 2 and 100")
  protected String address;

  @Size(min = 2, max = 30, message = "Length must between 2 and 30")
  protected String zip;
}
