package com.gabsw.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderLoadTestClient {

    private static final int TOTAL_REQUESTS = 1000;
    private static final String BASE_URL = "http://localhost:8080/api/orders";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Random random = new Random();
    private static final Logger log = LoggerFactory.getLogger(OrderLoadTestClient.class);

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        List<Future<Long>> futures = new ArrayList<>();

        log.info("Starting load test: {} requests to {}", TOTAL_REQUESTS, BASE_URL);

        // This avoids blocking real OS threads, even if requests wait for responses
        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            futures.add(executor.submit(() -> {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(generateOrderJson()))
                        .build();

                long start = System.nanoTime();

                // Send the request synchronously (blocking) — virtual threads make this cheap
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                long duration = System.nanoTime() - start;

                if (response.statusCode() != 200) {
                    log.warn("Request failed with status: {}", response.statusCode());
                }

                return duration;
            }));
        }

        // Wait for all tasks to complete and gather their durations
        long totalTime = 0;
        for (Future<Long> future : futures) {
            totalTime += future.get(); // This is blocking, but each future ran in its own thread
        }

        double avgTimeMs = totalTime / 1_000_000.0 / TOTAL_REQUESTS;
        log.info("Completed {} requests. Avg latency: {:.2f} ms", TOTAL_REQUESTS, avgTimeMs);

        executor.shutdown();
    }

    private static String generateOrderJson() {
        String[] tickers = {"AAPL", "GOOG", "TSLA"};
        String ticker = tickers[random.nextInt(tickers.length)];
        int quantity = random.nextInt(50) + 1;
        double price = 95 + random.nextDouble() * 10;
        String type = random.nextBoolean() ? "BUY" : "SELL";

        ObjectNode json = objectMapper.createObjectNode();
        json.put("ticker", ticker);
        json.put("quantity", quantity);
        json.put("price", price);
        json.put("type", type);

        return json.toString();
    }
}
