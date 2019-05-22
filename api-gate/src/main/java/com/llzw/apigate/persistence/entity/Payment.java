package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Payment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  protected Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orderId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  protected Order order;

  @Transient
  @JsonProperty
  protected String orderString;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payerId")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
  @JsonIdentityReference(alwaysAsId = true)
  protected User payer;

  @NotNull
  @Column(nullable = false)
  protected String subject;

  protected String description;

  protected float totalAmount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @NonNull
  protected PaymentStatusType status;

  protected String vendorTradeId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date confirmedAt;

  protected boolean confirmed;

  protected boolean valid;

  public enum PaymentStatusType {
    PENDING,
    CONFIRMED,
    TIMEOUT,
    INVALID
  }

  public boolean belongsToUser(User user) {
    return payer.getUsername().equals(user.getUsername()) || order.belongsToUser(user);
  }
}
