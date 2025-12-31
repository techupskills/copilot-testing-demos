package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.client.NotificationClient;
import com.example.demo.client.InventoryClient;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final NotificationClient notificationClient;
    private final InventoryClient inventoryClient;
    
    public OrderService(OrderRepository orderRepository, 
                       NotificationClient notificationClient,
                       InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.notificationClient = notificationClient;
        this.inventoryClient = inventoryClient;
    }
    
    public Order createOrder(String customerId, String productId, int quantity) {
        // Check inventory
        if (!inventoryClient.checkAvailability(productId, quantity)) {
            throw new IllegalStateException("Insufficient inventory for product: " + productId);
        }
        
        // Calculate price
        BigDecimal unitPrice = inventoryClient.getPrice(productId);
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        
        // Create and save order
        Order order = new Order(customerId, productId, quantity, totalPrice);
        Order savedOrder = orderRepository.save(order);
        
        // Send notification
        notificationClient.sendOrderConfirmation(customerId, savedOrder.getId());
        
        return savedOrder;
    }
    
    public Optional<Order> getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }
    
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        // Restore inventory
        inventoryClient.restoreInventory(order.getProductId(), order.getQuantity());
        
        // Delete order
        orderRepository.delete(order);
        
        // Send cancellation notification
        notificationClient.sendOrderCancellation(order.getCustomerId(), orderId);
    }
}
