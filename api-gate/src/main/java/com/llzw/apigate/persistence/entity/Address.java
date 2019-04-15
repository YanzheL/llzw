package com.llzw.apigate.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
public class Address extends AddressBean {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  @Getter
  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ownerId")
  protected User owner;
}
