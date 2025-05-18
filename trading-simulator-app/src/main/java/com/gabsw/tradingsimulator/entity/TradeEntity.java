package com.gabsw.tradingsimulator.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class TradeEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false)
    private OrderEntity buyOrder;

    @ManyToOne(optional = false)
    private OrderEntity sellOrder;

    @ManyToOne(optional = false)
    private InstrumentEntity instrument;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private long timestamp;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private long updatedAt;
}
