package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Order extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected UUID id;

  @Positive
  protected int quantity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customerId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
  @JsonIdentityReference(alwaysAsId = true)
  protected User customer;

  @Embedded
  protected AddressBean address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  protected Product product;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  protected List<Stock> stocks;

  protected String trackingId;

  protected String carrierName;

  protected float totalAmount;

  protected String remark;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date shippingTime;

  protected boolean deliveryConfirmed;

  protected boolean paid;

  protected boolean valid;

  public static Specification<Order> belongsToUserSpec(User user) {
    return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
        criteriaBuilder.equal(
            root.get("product").<User>get("seller").<String>get("username"),
            user.getUsername()
        ),
        criteriaBuilder.equal(
            root.get("customer").<String>get("username"), user.getUsername()
        )
    );
  }

  public boolean belongsToSeller(User seller) {
    return product.getSeller().getUsername().equals(seller.getUsername());
  }

  public boolean belongsToUser(User user) {
    return belongsToCustomer(user) || belongsToSeller(user);
  }

  public boolean belongsToCustomer(User customer) {
    return this.customer.getUsername().equals(customer.getUsername());
  }

  @PrePersist
  public void prePersist() {
    product.getStat().setSalesOutDated(true);
  }
}
