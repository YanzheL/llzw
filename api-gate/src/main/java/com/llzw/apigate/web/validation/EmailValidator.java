package com.llzw.apigate.web.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

  private static Pattern pattern =
      Pattern.compile(
          "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
              + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

  @Override
  public void initialize(final ValidEmail constraintAnnotation) {
  }

  @Override
  public boolean isValid(final String username, final ConstraintValidatorContext context) {
    return (validateEmail(username));
  }

  private boolean validateEmail(final String email) {
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }
}
