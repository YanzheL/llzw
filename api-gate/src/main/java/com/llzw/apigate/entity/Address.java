package com.llzw.apigate.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Address implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  protected Buyer owner;

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
