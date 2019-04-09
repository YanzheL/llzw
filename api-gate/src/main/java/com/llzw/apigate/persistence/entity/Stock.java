package com.llzw.apigate.persistence.entity;

import java.io.Serializable;
import java.util.Date;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
public class Stock implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  protected boolean valid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productId")
  protected Product productId;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected Date createdAt;

  @UpdateTimestamp
  protected Date updatedAt;

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

//stock.productId.seller.getUsername().equals(seller.getUsername());库存对应的产品的商家的名称要对应库存卖家的名称
 //库存的商品id要对应商品的id
  public boolean belongsToProduct(Product product) {
    return product.id.equals(productId.getId());
  }

}
