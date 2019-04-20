package com.llzw.apigate.web.dto;

import com.llzw.apigate.persistence.entity.User;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressCreateDto {

  @NotNull
  protected Long id;

  @NotNull
  protected User ownerId;

  @NotNull
  protected String province;

  @NotNull
  protected String city;

  @NotNull
  protected String district;

  @NotNull
  protected String address;

  @NotNull
  protected String zip;
}
