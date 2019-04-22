package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
public class Order implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  protected Long id;

  @Positive
  protected int quantity;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date createdAt;

  @UpdateTimestamp
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customerId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
  @JsonIdentityReference(alwaysAsId = true)
  protected User customer;

  @Embedded
  protected AddressBean address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stockId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  protected Stock stock;

  protected String trackingId;

  protected String carrierName;

  protected float totalAmount;

  protected String remark;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date shippingTime;

  protected boolean deliveryConfirmed;

  protected boolean paid;

  protected boolean valid;

  public boolean belongsToSeller(User seller) {
    return stock.product.seller.getUsername().equals(seller.getUsername());
  }

  public boolean belongsToUser(User user) {
    return belongsToCustomer(user) || belongsToSeller(user);
  }

  public boolean belongsToCustomer(User customer) {
    return this.customer.getUsername().equals(customer.getUsername());
  }
}
