package com.llzw.apigate.web.dto;

import com.llzw.apigate.web.validation.ValidPassword;
import com.llzw.apigate.web.validation.ValidRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDto {
  @NotNull
  @Size(min = 5, max = 30, message = "Length should between 5 to 30")
  protected String username;

  @ValidPassword protected String password;

  @NotNull
  @Size(min = 5, max = 100, message = "Length should between 5 to 100")
  protected String nickname;

  @NotNull @Email protected String email;

  @NotNull
  @Size(min = 5, max = 20, message = "Length should between 5 to 20")
  protected String phoneNumber;

  @ValidRole protected String role;
}
