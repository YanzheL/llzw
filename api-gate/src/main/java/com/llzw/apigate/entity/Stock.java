package com.llzw.apigate.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
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
    protected Integer shelfLife;

    @Column(nullable = false)
    @NonNull
    protected Integer totalQuantity;

    @Column(nullable = false)
    @NonNull
    protected Integer currentQuantity;

    @Column(length = 50)
    protected String trackingId;

    @Column(length = 50)
    protected String carrierName;
}
