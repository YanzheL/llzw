package com.llzw.apigate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
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
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    @Column(nullable = false)
    @NonNull
    protected LocalDateTime producedAt;

    protected LocalDateTime inboundedAt;

    @Column(nullable = false)
    @NonNull
    protected Integer shelfLife;

    @Column(nullable = false)
    @NonNull
    protected Integer totalQuantity;

    @Column(nullable = false)
    @NonNull
    protected Integer currentQuantity;

    protected String trackingId;

    protected String carrierName;
}

