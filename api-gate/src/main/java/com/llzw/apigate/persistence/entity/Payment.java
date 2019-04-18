package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
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
public class Payment implements Serializable {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orderId")
  protected Order order;

  @JsonInclude
  @Transient
  protected String orderString;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected Date createdAt;

  @UpdateTimestamp
  protected Date updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payerId")
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

  protected Date confirmedAt;

  protected boolean confirmed;

  protected boolean valid;

  public enum PaymentStatusType {
    PENDING,
    CONFIRMED,
    TIMEOUT,
    INVALID
  }
}
