package com.gabsw.tradingsimulator.service;

import com.gabsw.tradingsimulator.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService {
    private final List<Order> orders = new ArrayList<>();

    private final OrderMatchingService orderMatchingService;

    public OrderService(OrderMatchingService orderMatchingService) {
        this.orderMatchingService = orderMatchingService;
    }

    public Order placeOrder(Order order) {
        orders.add(order);
        orderMatchingService.submitOrder(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
