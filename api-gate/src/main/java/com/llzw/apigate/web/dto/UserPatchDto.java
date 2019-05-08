package com.llzw.apigate.web.dto;

import com.llzw.apigate.web.validation.ValidEmail;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPatchDto {

  @Size(max = 100, message = "Length cannot exceed 100")
  protected String nickname;

  @ValidEmail(message = "Invalid email")
  protected String email;

  @Size(min = 5, max = 20, message = "Length must between 5 and 20")
  @Digits(integer = 20, fraction = 0, message = "Content can only contain digits")
  protected String phoneNumber;

  @Size(max = 200, message = "Length cannot exceed 200")
  protected String avatar;
}
