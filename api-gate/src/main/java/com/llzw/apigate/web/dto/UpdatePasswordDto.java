package com.llzw.apigate.web.dto;

import com.llzw.apigate.web.validation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdatePasswordDto {
  @NotNull private String oldPassword;
  @ValidPassword private String newPassword;
}
