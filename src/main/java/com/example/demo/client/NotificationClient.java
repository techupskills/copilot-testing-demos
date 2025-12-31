package com.example.demo.client;

public interface NotificationClient {
    void sendOrderConfirmation(String customerId, Long orderId);
    void sendOrderCancellation(String customerId, Long orderId);
}
