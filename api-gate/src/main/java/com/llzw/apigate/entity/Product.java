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
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    protected Seller seller;

    @Column(nullable = false)
    @NonNull
    protected String name;

    @Column(nullable = false)
    @NonNull
    protected String introduction;

    @CreationTimestamp
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    @Column(nullable = false)
    @NonNull
    protected Float price;

    protected Integer maxDeliveryHours;

    @Column(nullable = false)
    @NonNull
    protected String ca;

    @Column(nullable = false)
    @NonNull
    protected String caFile;

    @Column(nullable = false)
    @NonNull
    protected String caId;


}
