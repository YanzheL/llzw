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
import javax.persistence.Lob;
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
public class Product implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  protected Long id;

  @Setter()
  @NonNull
  protected boolean valid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sellerId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
  @JsonIdentityReference(alwaysAsId = true)
  protected User seller;

  @Column(nullable = false)
  @NonNull
  protected String name;

  @Lob
  @NonNull
  protected String introduction;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date createdAt;

  @UpdateTimestamp
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date updatedAt;

  @Column(nullable = false)
  @NonNull
  protected float price;

  @Column(nullable = false, length = 50)
  @NonNull
  protected String ca;

  @Column(nullable = false)
  @NonNull
  protected String caFile;

  @Column(nullable = false, length = 50)
  @NonNull
  protected String caId;

  public boolean belongsToSeller(User seller) {
    return this.seller.getUsername().equals(seller.getUsername());
  }
}
