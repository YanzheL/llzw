package com.llzw.apigate.web.dto;

import com.llzw.apigate.web.validation.ValidPassword;
import com.llzw.apigate.web.validation.ValidRole;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

  @Pattern(regexp = "[a-zA-Z]\\w{5,50}", message = "Username must fulfill regex pattern [a-zA-Z]\\w{5,50}")
  protected String username;

  @ValidPassword
  protected String password;

  @Size(max = 100, message = "Length cannot exceed 100")
  protected String nickname;

  @Email(message = "Invalid email")
  protected String email;

  @Size(min = 5, max = 20, message = "Length must between 5 to 20")
  @Digits(integer = 20, fraction = 0, message = "Content can only contain digits")
  protected String phoneNumber;

  @ValidRole
  protected String role;
}
