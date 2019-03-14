package com.llzw.apigate.service;

import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.Role;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleUserService implements UserService {

  private SessionRegistry sessionRegistry;
  private PasswordEncoder passwordEncoder;
  private UserRepository userRepository;

  @Autowired
  public SimpleUserService(
      SessionRegistry sessionRegistry,
      PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.sessionRegistry = sessionRegistry;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

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
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(Collections.singletonList(new Role(userDto.getRole())));

    return userRepository.save(user);
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
}
