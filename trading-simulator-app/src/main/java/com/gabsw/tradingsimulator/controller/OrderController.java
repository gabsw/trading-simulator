package com.gabsw.tradingsimulator.controller;

import com.gabsw.tradingsimulator.dto.OrderDTO;
import com.gabsw.tradingsimulator.mapper.OrderMapper;
import com.gabsw.tradingsimulator.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public OrderDTO placeOrder(@RequestBody @Valid OrderDTO orderDTO) {
        var order = orderMapper.toDomain(orderDTO);
        var placedOrder = orderService.placeOrder(order);
        return orderMapper.toDto(placedOrder);
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        var orders = orderService.getAllOrders();
        return orderMapper.toDtoList(orders);
    }
}
