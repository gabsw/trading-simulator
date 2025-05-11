package com.gabsw.tradingsimulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String ticker;
    private int quantity;
    private double price;
    private OrderType type;

    public enum OrderType {
        BUY, SELL
    }
}
