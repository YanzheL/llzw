package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
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
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

@Indexed
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

  protected boolean valid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sellerId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
  @JsonIdentityReference(alwaysAsId = true)
  protected User seller;

  @JsonGetter("seller")
  public Map<String, String> getSellerMeta() {
    Map<String, String> res = new HashMap<>();
    res.put("username", seller.getUsername());
    res.put("nickname", seller.getNickname());
    return res;
  }

  @Column(nullable = false)
  @Field(termVector = TermVector.YES)
  protected String name;

  @ElementCollection(fetch = FetchType.EAGER)
  protected List<String> mainImageFiles;

  @Lob
  @Field(termVector = TermVector.YES)
  protected String introduction;

  @Column(nullable = false)
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

  protected String category;

  protected String feature;

  @Embedded
  protected ProductStat stat;

  public boolean belongsToSeller(User seller) {
    return this.seller.getUsername().equals(seller.getUsername());
  }
}
