package com.gabsw.tradingsimulator.dto;

import com.gabsw.tradingsimulator.model.Order.OrderType;
import lombok.Data;

@Data
public class OrderDTO {
    private String ticker;
    private int quantity;
    private double price;
    private OrderType type;
}
