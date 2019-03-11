package com.llzw.apigate.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode(callSuper = true)
public class Seller extends User {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  @NonNull
  protected Address contactAddress;
}
