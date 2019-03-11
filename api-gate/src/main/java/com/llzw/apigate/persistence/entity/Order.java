package com.llzw.apigate.persistence.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Order implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  @NotNull protected Integer quantity;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected Date createdAt;

  @UpdateTimestamp protected Date updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  protected User customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  protected Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stock_id")
  protected Stock stock;
}
