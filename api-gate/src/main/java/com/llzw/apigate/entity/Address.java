package com.llzw.apigate.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
//@Inheritance(strategy = InheritanceType.JOINED)
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Buyer owner;

    @Column(nullable = false)
    @NonNull
    protected String province;

    @Column(nullable = false)
    @NonNull
    protected String city;

    @Column(nullable = false)
    @NonNull
    protected String district;

    @Column(nullable = false)
    @NonNull
    protected String address;

    protected String zip;
}
