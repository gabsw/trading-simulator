package com.gabsw.tradingsimulator.service;

import com.gabsw.tradingsimulator.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final List<Order> orders = new ArrayList<>();

    public Order placeOrder(Order order) {
        orders.add(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
