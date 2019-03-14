package com.llzw.apigate.service;

import com.llzw.apigate.persistence.dao.RoleRepository;
import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.Role;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.RealNameVerificationDto;
import com.llzw.apigate.web.dto.UserDto;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class SimpleUserService implements UserService {

  @Setter(onMethod_ = @Autowired)
  private SessionRegistry sessionRegistry;

  @Setter(onMethod_ = @Autowired)
  private PasswordEncoder passwordEncoder;

  @Setter(onMethod_ = @Autowired)
  private RoleRepository roleRepository;

  @Setter(onMethod_ = @Autowired)
  private UserRepository userRepository;

  @Override
  @Transactional
  public boolean register(UserDto userDto, Collection<String> msgs) throws EntityExistsException {
    String username = userDto.getUsername();
    String email = userDto.getEmail();
    if (userRepository.findByEmail(email).isPresent()) {
      msgs.add("There is an account with that email adress: " + userDto.getEmail());
      return false;
    }
    if (userRepository.findByUsername(username).isPresent()) {
      msgs.add("There is an account with that username: " + userDto.getUsername());
      return false;
    }
    final User user = new User();
    BeanUtils.copyProperties(userDto, user, "role", "password");
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    Optional<Role> roleOptional =
        roleRepository.findByRole(Role.RoleType.valueOf(userDto.getRole()));
    if (!roleOptional.isPresent()) {
      msgs.add("Role doesn't exist");
      return false;
    }
    user.setRoles(Collections.singleton(roleOptional.get()));
    user.setEnabled(true);
    userRepository.save(user);
    return true;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByUsername(username);
    userOptional.orElseThrow(
        () -> new UsernameNotFoundException("No user found with username: " + username));
    User user = userOptional.get();

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        user.isEnabled(),
        user.isAccountNonExpired(),
        user.isCredentialsNonExpired(),
        user.isAccountNonLocked(),
        flattenAllAuthorities(user));
  }

  //  @Override
  //  public Optional<User> findUserByUsername(String username) {
  //    return userRepository.findByUsername(username);
  //  }
  //
  //  @Override
  //  public Optional<User> findUserByEmail(String email) {
  //    return userRepository.findByEmail(email);
  //  }

  @Override
  public boolean setUserPassword(String username, String password, Collection<String> msgs) {
    return applyToUser(
        username,
        user -> {
          user.setPassword(passwordEncoder.encode(password));
          return true;
        },
        msgs);
  }

  @Override
  public boolean updateUserPassword(
      String username, String oldPassword, String newPassword, Collection<String> msgs) {
    return applyToUser(
        username,
        user -> {
          if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
            msgs.add("Password doesn't match");
            return false;
          }
          user.setPassword(passwordEncoder.encode(newPassword));
          return true;
        },
        msgs);
  }

  @Override
  public List<User> getUsersFromSessionRegistry() {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty() && u instanceof User)
        .map(u -> (User) u)
        .collect(Collectors.toList());
  }

  @Override
  public boolean realNameVerification(
      String username, RealNameVerificationDto realNameVerificationDto, Collection<String> msgs) {
    return applyToUser(
        username,
        user -> {
          BeanUtils.copyProperties(realNameVerificationDto, user);
          if (user.isVerified()) {
            msgs.add("User is already verified");
            return false;
          }
          user.setVerified(true);
          return true;
        },
        msgs);
  }

  private Collection<? extends GrantedAuthority> flattenAllAuthorities(User user) {
    Collection<GrantedAuthority> all =
        user.getRoles().stream()
            .map(Role::getRole)
            .map(Role.RoleType::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    all.addAll(user.getAuthorities());
    return all;
  }

  private boolean applyToUser(String username, Predicate<User> consumer, Collection<String> msgs) {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      User found = userOptional.get();
      boolean success = consumer.test(found);
      if (success) userRepository.save(found);
      return success;
    } else {
      msgs.add("User doesn't exist");
      return false;
    }
  }
}
