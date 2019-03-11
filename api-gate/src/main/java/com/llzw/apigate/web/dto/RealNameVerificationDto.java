package com.llzw.apigate.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RealNameVerificationDto {
  @NotNull
  @Size(min = 1, max = 10, message = "Length should between 1 to 10")
  protected String identityType;

  @NotNull
  @Size(min = 1, max = 20, message = "Length should between 1 to 20")
  protected String identityNumber;

  @NotNull
  @Size(min = 1, max = 20, message = "Length should between 1 to 20")
  protected String firstName;

  @NotNull
  @Size(min = 1, max = 20, message = "Length should between 1 to 20")
  protected String lastName;
}
