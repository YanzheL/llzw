package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class User extends BaseEntity implements UserDetails {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 30)
  protected String username;

  @Column(nullable = false)
  @NonNull
  @JsonIgnore
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

  protected boolean enabled;

  protected boolean verified;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "roleId"))
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
    Collection<GrantedAuthority> all = getPrivilegesAsGrantedAuthorities();
    all.addAll(getRolesAsGrantedAuthorities());
    return all;
  }

  public Collection<GrantedAuthority> getPrivilegesAsGrantedAuthorities() {
    return roles.stream()
        .map(Role::getPrivilegeNames)
        .flatMap(Collection::stream)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  public Collection<GrantedAuthority> getRolesAsGrantedAuthorities() {
    return roles.stream()
        .map(Role::getRole)
        .map(Role.RoleType::name)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  public Collection<String> getRoleNames() {
    return roles.stream().map(Role::toString).collect(Collectors.toList());
  }

  public boolean hasRole(String name) {
    return getRoleNames().contains(name);
  }

  public boolean hasRole(Role.RoleType type) {
    return hasRole(type.name());
  }

  public enum IdType {
    PRC_ID,
    PASSPORT
  }
}
