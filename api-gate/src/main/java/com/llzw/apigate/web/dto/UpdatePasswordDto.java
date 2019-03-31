package com.llzw.apigate.web.dto;

import com.llzw.apigate.web.validation.ValidPassword;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePasswordDto {

  @NotNull
  private String oldPassword;
  @ValidPassword
  private String newPassword;
}
