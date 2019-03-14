package com.llzw.apigate.persistence.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

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
  @Column(length = 10)
  protected IdType identityType;

  @Column(length = 20)
  protected String identityNumber;

  @Column(length = 10)
  @NonNull
  protected String firstName;

  @Column(length = 10)
  @NonNull
  protected String lastName;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected Date createdAt;

  @UpdateTimestamp protected Date updatedAt;

  protected boolean enabled;

  protected boolean verified;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  protected Collection<Role> roles = new ArrayList<>();

  @OneToMany(mappedBy = "owner")
  protected Collection<Address> addresses = new ArrayList<>();

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
    return enabled;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(Role::getPrivilegeNames)
        .flatMap(Collection::stream)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  public Collection<String> getRoleNames() {
    return roles.stream().map(Role::toString).collect(Collectors.toList());
  }

  public enum IdType {
    PRC_ID,
    PASSPORT
  }
}
