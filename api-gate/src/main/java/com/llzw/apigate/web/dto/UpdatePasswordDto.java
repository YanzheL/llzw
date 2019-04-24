package com.llzw.apigate.web.dto;

import com.llzw.apigate.web.validation.ValidPassword;
import lombok.Data;

@Data
public class UpdatePasswordDto {

  @ValidPassword
  private String oldPassword;
  @ValidPassword
  private String newPassword;
}
