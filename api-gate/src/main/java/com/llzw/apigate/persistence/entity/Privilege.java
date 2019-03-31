package com.llzw.apigate.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

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

  public Privilege(PrivilegeType type) {
    privilege = type;
  }

  @Override
  public String toString() {
    return privilege.name();
  }

  public enum PrivilegeType {
    OP_CREATE_ORDER,
    OP_READ_ORDER,
    OP_UPDATE_ORDER,
    OP_DELETE_ORDER,

    OP_CREATE_PRODUCT,
    OP_READ_PRODUCT,
    OP_UPDATE_PRODUCT,
    OP_DELETE_PRODUCT,

    OP_CREATE_STOCK,
    OP_READ_STOCK,
    OP_UPDATE_STOCK,
    OP_DELETE_STOCK,
    OP_MANAGE_PASSWORD
  }
}
