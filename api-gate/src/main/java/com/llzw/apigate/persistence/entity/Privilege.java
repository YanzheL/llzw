package com.llzw.apigate.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Privilege implements Serializable {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @NonNull
  protected PrivilegeType privilege;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Setter(AccessLevel.NONE)
  private Long id;

  @ManyToMany(mappedBy = "privileges", cascade = CascadeType.ALL)
  private Collection<Role> roles = new ArrayList<>();

  @Override
  public String toString() {
    return privilege.name();
  }

  public Privilege(PrivilegeType type) {
    privilege = type;
  }

  public enum PrivilegeType {
    CREATE_ORDER,
    READ_ORDER,
    UPDATE_ORDER,
    DELETE_ORDER,

    CREATE_PRODUCT,
    READ_PRODUCT,
    UPDATE_PRODUCT,
    DELETE_PRODUCT,

    CREATE_STOCK,
    READ_STOCK,
    UPDATE_STOCK,
    DELETE_STOCK,

    MANAGE_PASSWORD,
  }
}
