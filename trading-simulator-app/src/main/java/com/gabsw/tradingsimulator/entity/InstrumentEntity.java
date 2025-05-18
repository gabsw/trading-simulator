package com.gabsw.tradingsimulator.entity;

import com.gabsw.tradingsimulator.domain.enums.InstrumentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class InstrumentEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String ticker;

    @Column(nullable = false)
    private String name;

    private String exchange;
    private String currency;
    private String sector;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InstrumentType type;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private long updatedAt;
}
