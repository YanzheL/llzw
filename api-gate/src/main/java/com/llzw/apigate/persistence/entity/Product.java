package com.llzw.apigate.persistence.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Product implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  protected boolean valid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id")
  protected User seller;

  @Column(nullable = false)
  @NonNull
  protected String name;

  @Lob @NonNull protected String introduction;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected Date createdAt;

  @UpdateTimestamp protected Date updatedAt;

  @Column(nullable = false)
  @NonNull
  protected float price;

  protected Integer maxDeliveryHours;

  @Column(nullable = false, length = 10)
  @NonNull
  protected String ca;

  @Column(nullable = false)
  @NonNull
  protected String caFile;

  @Column(nullable = false, length = 50)
  @NonNull
  protected String caId;
}
