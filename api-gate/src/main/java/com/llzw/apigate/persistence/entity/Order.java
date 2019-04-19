package com.llzw.apigate.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
public class Order implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  @Positive
  protected int quantity;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected Date createdAt;

  @UpdateTimestamp
  protected Date updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customerId")
  protected User customer;

  @Embedded
  protected AddressBean address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stockId")
  protected Stock stock;

  protected String trackingId;

  protected String carrierName;

  protected float totalAmount;

  protected String remark;

  protected Date shippingTime;

  protected boolean deliveryConfirmed;

  protected boolean paid;

  protected boolean valid;

  public boolean belongsToSeller(User seller) {
    return stock.productId.seller.getUsername().equals(seller.getUsername());
  }

  public boolean belongsToUser(User user) {
    return belongsToCustomer(user) || belongsToSeller(user);
  }

  public boolean belongsToCustomer(User customer) {
    return this.customer.getUsername().equals(customer.getUsername());
  }
}
