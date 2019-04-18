package com.llzw.apigate.persistence.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
public class Address implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ownerId")
  protected User owner;

  @Column(nullable = false, length = 20)
  @NonNull
  protected String province;

  @Column(nullable = false, length = 20)
  @NonNull
  protected String city;

  @Column(nullable = false, length = 20)
  @NonNull
  protected String district;

  @Column(nullable = false)
  @NonNull
  protected String address;

  @Column(length = 6)
  protected String zip;

}
