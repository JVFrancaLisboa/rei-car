package com.reicar.entities;

import com.reicar.entities.enums.ServiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "service_orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "service_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceItem> items = new ArrayList<>();

    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal serviceValue = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalValue = BigDecimal.ZERO;

    /**
     * Calcula o valor total da OS.
     * @param markup Fator de acréscimo sobre as peças (ex: 1.30 para 30%)
     */
    public void calculateTotalValue(double markup) {
        // Calcula o total das peças com markup
        BigDecimal itemsTotal = items.stream()
                .map(item -> item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                        .multiply(BigDecimal.valueOf(markup)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Soma a mão de obra (serviceValue) ao total das peças
        this.totalValue = itemsTotal.add(this.serviceValue)
                .setScale(2, RoundingMode.HALF_UP);
    }
}