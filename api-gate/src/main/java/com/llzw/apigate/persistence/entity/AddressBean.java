package com.llzw.apigate.persistence.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@MappedSuperclass
@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class AddressBean implements Serializable {

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
