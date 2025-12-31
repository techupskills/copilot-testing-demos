package com.example.demo.repository;

import com.example.demo.model.Order;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    void delete(Order order);
}
