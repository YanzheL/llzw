package com.llzw.apigate.entity;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 30)
  protected String username;

  @Column(nullable = false)
  @NonNull
  protected String password;

  @Column(nullable = false, length = 100)
  @NonNull
  protected String nickname;

  @Column(nullable = false)
  @NonNull
  protected String email;

  @Column(nullable = false, length = 20)
  @NonNull
  protected String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  @NonNull
  protected IdType identity_type;

  @Column(nullable = false, length = 20)
  @NonNull
  protected String identity_number;

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  enum IdType {
    PRC_ID,
    PASSPORT
  }
}
