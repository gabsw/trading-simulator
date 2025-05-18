package com.gabsw.tradingsimulator.domain;

import com.gabsw.tradingsimulator.domain.enums.OrderStatus;
import com.gabsw.tradingsimulator.domain.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID id;
    private String ticker;
    private int quantity;
    private double price;
    private OrderType type;
    private OrderStatus status;
}
