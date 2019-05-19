package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Stock extends BaseEntity {

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

  @Column(nullable = false)
  @NonNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date producedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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

  @Transactional
  public boolean belongsToSeller(User seller) {
    return product.getSeller().getUsername().equals(seller.getUsername());
  }

  public boolean decreaseCurrentQuantity(int quantity) {
    if (currentQuantity >= quantity) {
      currentQuantity -= quantity;
    }
    return currentQuantity == 0;
  }

  @PrePersist
  public void prePersist() {
    product.getStat().setStocksOutDated(true);
  }
}
