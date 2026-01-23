package com.reicar.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "service_items")
public class ServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    private String description;
    private BigDecimal unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceOrder serviceOrder;
}
