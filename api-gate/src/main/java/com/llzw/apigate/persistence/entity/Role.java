package com.llzw.apigate.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
// @AllArgsConstructor
public class Role implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  protected Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @NonNull
  protected RoleType role;

  @ManyToMany(mappedBy = "roles")
  protected Collection<User> users = new ArrayList<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      joinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "privilegeId", referencedColumnName = "id"))
  protected Collection<Privilege> privileges = new ArrayList<>();

  public Role(String role) {
    this.role = RoleType.valueOf(role);
  }

  public Role(RoleType role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return role.name();
  }

  public Collection<String> getPrivilegeNames() {
    return privileges.stream().map(Privilege::toString).collect(Collectors.toList());
  }

  public enum RoleType {
    ROLE_SELLER,
    ROLE_CUSTOMER
  }
}
