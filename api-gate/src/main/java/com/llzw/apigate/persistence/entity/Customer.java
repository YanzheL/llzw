package com.llzw.apigate.persistence.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {
  private static final long serialVersionUID = 1L;

  @OneToMany(mappedBy = "owner")
  protected Collection<Address> addresses;
}
