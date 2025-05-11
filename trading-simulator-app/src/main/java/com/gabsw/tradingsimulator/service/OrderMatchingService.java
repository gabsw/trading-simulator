package com.gabsw.tradingsimulator.service;

import com.gabsw.tradingsimulator.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class OrderMatchingService {

    private final Map<String, PriorityQueue<Order>> buyOrders = new ConcurrentHashMap<>();
    private final Map<String, PriorityQueue<Order>> sellOrders = new ConcurrentHashMap<>();

    // Per-ticker lock objects to avoid race conditions
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    // Virtual thread executor — creates a new virtual thread per task
    // Unlike traditional thread pools, this allows scaling to thousands of tasks with minimal overhead
    private final ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    // Entry point: each incoming order is submitted to the executor
    public void submitOrder(Order order) {
        // This runs the matching logic for each order on its own virtual thread
        // Safe to do blocking operations inside matchOrder if needed (e.g., DB calls, sleeps)
        virtualExecutor.submit(() -> matchOrder(order));
    }

    // Core matching logic — executed in a virtual thread
    private void matchOrder(Order incomingOrder) {
        String ticker = incomingOrder.getTicker();

        // Ensure a lock object exists for this ticker
        locks.putIfAbsent(ticker, new Object());

        // Synchronize matching logic per ticker to avoid concurrent access to the same order book
        // Multiple tickers can be processed in parallel across virtual threads
        synchronized (locks.get(ticker)) {

            PriorityQueue<Order> buyQueue = buyOrders.computeIfAbsent(ticker, k -> new PriorityQueue<>(
                    (o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice())
            ));

            PriorityQueue<Order> sellQueue = sellOrders.computeIfAbsent(ticker, k -> new PriorityQueue<>(
                    Comparator.comparingDouble(Order::getPrice)
            ));

            if (incomingOrder.getType() == Order.OrderType.BUY) {
                buyQueue.offer(incomingOrder);
            } else {
                sellQueue.offer(incomingOrder);
            }

            while (!buyQueue.isEmpty() && !sellQueue.isEmpty()) {
                Order buy = buyQueue.peek();
                Order sell = sellQueue.peek();

                if (buy.getPrice() >= sell.getPrice()) {
                    int tradedQuantity = Math.min(buy.getQuantity(), sell.getQuantity());
                    double tradePrice = sell.getPrice();
                    log.info("Matched {} shares of {} at ${}", tradedQuantity, ticker, tradePrice);

                    buy.setQuantity(buy.getQuantity() - tradedQuantity);
                    sell.setQuantity(sell.getQuantity() - tradedQuantity);

                    if (buy.getQuantity() == 0) buyQueue.poll();
                    if (sell.getQuantity() == 0) sellQueue.poll();

                } else {
                    log.info("No more matches possible at current prices");
                    break;
                }
            }
        }
    }
}
