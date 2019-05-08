package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Product extends BaseEntity {

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

  @ElementCollection
  protected List<String> mainImageFiles;

  @Lob
  @NonNull
  protected String introduction;

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

  @Transactional
  public boolean belongsToSeller(User seller) {
    return this.seller.getUsername().equals(seller.getUsername());
  }
}
