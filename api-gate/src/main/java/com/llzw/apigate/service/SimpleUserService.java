package com.llzw.apigate.service;

import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.Privilege;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SimpleUserService implements UserService {

  @Setter(onMethod_ = @Autowired)
  private SessionRegistry sessionRegistry;

  @Setter(onMethod_ = @Autowired)
  private PasswordEncoder passwordEncoder;

  @Setter(onMethod_ = @Autowired)
  private UserRepository userRepository;

  @Override
  public User register(UserDto userDto) throws EntityExistsException {
    String username = userDto.getUsername();
    String email = userDto.getEmail();
    if (findUserByEmail(email).isPresent()) {
      throw new EntityExistsException(
          "There is an account with that email adress: " + userDto.getEmail());
    }
    if (findUserByUsername(username).isPresent()) {
      throw new EntityExistsException(
          "There is an account with that username: " + userDto.getUsername());
    }
    final User user = new User();
    BeanUtils.copyProperties(userDto, user, "role", "password");
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setRoles(Collections.singletonList(new Role(userDto.getRole())));

    return userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

    try {
      Optional<User> userOptional = findUserByUsername(username);
      userOptional.orElseThrow(
          () -> new UsernameNotFoundException("No user found with username: " + username));

      User user = userOptional.get();

      return new org.springframework.security.core.userdetails.User(
          user.getUsername(),
          user.getPassword(),
          user.isEnabled(),
          true,
          true,
          true,
          getAuthorities(user.getRoles()));
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<User> findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public boolean changeUserPassword(String username, String password) {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      User found = userOptional.get();
      found.setPassword(passwordEncoder.encode(password));
      userRepository.save(found);
      return true;
    }
    return false;
  }

  @Override
  public List<User> getUsersFromSessionRegistry() {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty() && u instanceof User)
        .map(u -> (User) u)
        .collect(Collectors.toList());
  }

  @Override
  public void realNameVerification(
      String username, RealNameVerificationDto realNameVerificationDto) {
    Optional<User> userOptional = findUserByUsername(username);
    userOptional.orElseThrow(() -> new RuntimeException("User doesn't exist"));
    User user = userOptional.get();
    BeanUtils.copyProperties(realNameVerificationDto, user);
    user.setVerified(true);
    userRepository.save(user);
  }

  private final Collection<? extends GrantedAuthority> getAuthorities(
      final Collection<Role> roles) {
    return getGrantedAuthorities(getPrivileges(roles));
  }

  private final List<Privilege.PrivilegeType> getPrivileges(final Collection<Role> roles) {
    final List<Privilege.PrivilegeType> privileges = new ArrayList<>();
    final List<Privilege> collection = new ArrayList<>();
    for (final Role role : roles) {
      collection.addAll(role.getPrivileges());
    }
    for (final Privilege item : collection) {
      privileges.add(item.getPrivilege());
    }

    return privileges;
  }

  private List<GrantedAuthority> getGrantedAuthorities(
      final List<Privilege.PrivilegeType> privileges) {
    final List<GrantedAuthority> authorities = new ArrayList<>();
    for (final Privilege.PrivilegeType privilege : privileges) {
      authorities.add(new SimpleGrantedAuthority(privilege.name()));
    }
    return authorities;
  }
}
