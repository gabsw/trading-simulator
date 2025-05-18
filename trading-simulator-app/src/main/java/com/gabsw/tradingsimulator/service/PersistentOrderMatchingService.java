package com.gabsw.tradingsimulator.service;

import com.gabsw.tradingsimulator.domain.enums.OrderStatus;
import com.gabsw.tradingsimulator.domain.enums.OrderType;
import com.gabsw.tradingsimulator.entity.OrderEntity;
import com.gabsw.tradingsimulator.entity.TradeEntity;
import com.gabsw.tradingsimulator.repository.OrderRepository;
import com.gabsw.tradingsimulator.repository.TradeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersistentOrderMatchingService {

    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;

    private final ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public void submitOrder(UUID orderId) {
        virtualExecutor.submit(() -> matchOrder(orderId));
    }

    @Transactional
    protected void matchOrder(UUID orderId) {
        // It assumes that the order has already been posted, to be improved
        OrderEntity incomingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (incomingOrder.getStatus() != OrderStatus.NEW) return;

        List<OrderEntity> oppositeOrders = incomingOrder.getType() == OrderType.BUY
                ? orderRepository.findSellOrdersAtOrBelowPrice(
                        incomingOrder.getInstrument().getId(), OrderType.SELL, incomingOrder.getPrice()
        )
                : orderRepository.findBuyOrdersAtOrAbovePrice(
                        incomingOrder.getInstrument().getId(), OrderType.BUY, incomingOrder.getPrice()
        );

        for (OrderEntity counter : oppositeOrders) {
            if (incomingOrder.getQuantity() <= 0) break;

            int matchQty = Math.min(incomingOrder.getQuantity(), counter.getQuantity());
            BigDecimal tradePrice = counter.getPrice();

            TradeEntity trade = TradeEntity.builder()
                    .buyOrder(incomingOrder.getType() == OrderType.BUY ? incomingOrder : counter)
                    .sellOrder(incomingOrder.getType() == OrderType.SELL ? incomingOrder : counter)
                    .instrument(incomingOrder.getInstrument())
                    .quantity(matchQty)
                    .price(tradePrice)
                    .timestamp(System.currentTimeMillis())
                    .createdAt(System.currentTimeMillis())
                    .updatedAt(System.currentTimeMillis())
                    .build();

            tradeRepository.save(trade);

            incomingOrder.setQuantity(incomingOrder.getQuantity() - matchQty);
            counter.setQuantity(counter.getQuantity() - matchQty);

            if (counter.getQuantity() == 0) {
                counter.setStatus(OrderStatus.FILLED);
            } else {
                counter.setStatus(OrderStatus.PARTIALLY_FILLED);
            }
            orderRepository.save(counter);
        }

        incomingOrder.setStatus(
                incomingOrder.getQuantity() == 0 ? OrderStatus.FILLED : OrderStatus.PARTIALLY_FILLED);
        orderRepository.save(incomingOrder);

        log.info("Order {} matched with {} trades", incomingOrder.getId(), oppositeOrders.size());
    }
}