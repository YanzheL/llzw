package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
public class Stock implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  protected Long id;

  protected boolean valid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  protected Product product;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected Date createdAt;

  @UpdateTimestamp
  protected Date updatedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(nullable = false)
  @NonNull
  protected Date producedAt;

  protected Date inboundedAt;

  @Column(nullable = false)
  @NonNull
  protected int shelfLife;

  @Column(nullable = false)
  @NonNull
  protected int totalQuantity;

  @Column(nullable = false)
  @NonNull
  protected int currentQuantity;

  @Column(length = 50)
  protected String trackingId;

  @Column(length = 50)
  protected String carrierName;

  public boolean belongsToSeller(User seller) {
    return product.getSeller().getUsername().equals(seller.getUsername());
  }

  public boolean decreaseCurrentQuantity(int quantity) {
    if (currentQuantity >= quantity) {
      currentQuantity -= quantity;
    }
    return currentQuantity == 0;
  }
}
