package com.llzw.apigate.security;

import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.User;
import java.util.Optional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

// @Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

  @Setter(onMethod_ = @Autowired)
  private UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
    Optional<User> userOptional = userRepository.findByUsername(auth.getName());
    userOptional.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    User user = userOptional.get();
    final Authentication result = super.authenticate(auth);
    return new UsernamePasswordAuthenticationToken(
        user, result.getCredentials(), result.getAuthorities());
  }

  //  private boolean isValidLong(String code) {
  //    try {
  //      Long.parseLong(code);
  //    } catch (final NumberFormatException e) {
  //      return false;
  //    }
  //    return true;
  //  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
