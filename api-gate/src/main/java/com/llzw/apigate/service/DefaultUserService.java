package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestEntityExistsException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.message.error.RestInvalidCredentialException;
import com.llzw.apigate.persistence.dao.RoleRepository;
import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.Role;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.RealNameVerificationDto;
import com.llzw.apigate.web.dto.UserDto;
import com.llzw.apigate.web.dto.UserPatchDto;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultUserService implements UserService {

  @Setter(onMethod_ = @Autowired)
  private SessionRegistry sessionRegistry;

  @Setter(onMethod_ = @Autowired)
  private PasswordEncoder passwordEncoder;

  @Setter(onMethod_ = @Autowired)
  private RoleRepository roleRepository;

  @Setter(onMethod_ = @Autowired)
  private UserRepository userRepository;

  @Setter(onMethod_ = @Autowired)
  private FileStorageService fileStorageService;

  @Override
  public User register(UserDto dto) throws RestApiException {
    String username = dto.getUsername();
    String nickname = dto.getNickname();
    String email = dto.getEmail();
    if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
      throw new RestEntityExistsException("There is an account with same username or email email");
    }
    Role role = roleRepository.findByRole(Role.RoleType.valueOf(dto.getRole())).orElseThrow(
        () -> new RestEntityNotFoundException(
            String.format("Role <%s> does not exist", dto.getRole())
        )
    );
    final User user = new User();
    BeanUtils.copyProperties(dto, user, "role", "password", "nickname");
    user.setNickname(nickname == null || nickname.isEmpty() ? username : nickname);
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRoles(Collections.singleton(role));
    user.setEnabled(true);
    return userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).orElseThrow(
        () -> new UsernameNotFoundException("No user found with username: " + username)
    );
  }

  @Override
  public User setUserPassword(String username, String password)
      throws RestApiException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RestEntityNotFoundException("User doesn't exist"));
    user.setPassword(passwordEncoder.encode(password));
    return userRepository.save(user);
  }

  @Override
  public User updateUserPassword(String username, String oldPassword, String newPassword)
      throws RestApiException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RestEntityNotFoundException("User doesn't exist"));
    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new RestInvalidCredentialException("Password doesn't match");
    }
    user.setPassword(passwordEncoder.encode(newPassword));
    return userRepository.save(user);
  }

  @Override
  public List<User> getUsersFromSessionRegistry() {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty() && u instanceof User)
        .map(u -> (User) u)
        .collect(Collectors.toList());
  }

  @Override
  public User realNameVerification(String username, RealNameVerificationDto dto)
      throws RestApiException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RestEntityNotFoundException("User doesn't exist"));
    if (user.isVerified()) {
      return user;
    }
    BeanUtils.copyProperties(dto, user);
    user.setVerified(true);
    return userRepository.save(user);
  }

  @Override
  public User updateInfo(String username, UserPatchDto userPatchDto) throws RestApiException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RestEntityNotFoundException("User doesn't exist"));
    String oldAvatar = user.getAvatar();
    String newAvatar = userPatchDto.getAvatar();
    if (newAvatar != null && fileStorageService.isAcceptablePath(oldAvatar)) {
      fileStorageService.delete(oldAvatar);
    }
    BeanUtils.copyProperties(userPatchDto, user);
    if (fileStorageService.isAcceptablePath(newAvatar)) {
      fileStorageService.increaseReferenceCount(newAvatar);
    }
    return userRepository.save(user);
  }
}
