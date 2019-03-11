package com.llzw.apigate.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
// @AllArgsConstructor
public class Role implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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
      joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
  protected Collection<Privilege> privileges = new ArrayList<>();

  @Override
  public String toString() {
    return role.name();
  }

  public Collection<String> getPrivilegeNames() {
    return privileges.stream().map(Privilege::toString).collect(Collectors.toList());
  }

  public enum RoleType {
    SELLER,
    CUSTOMER
  }
}
