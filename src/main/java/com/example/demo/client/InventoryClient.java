package com.example.demo.client;

import java.math.BigDecimal;

public interface InventoryClient {
    boolean checkAvailability(String productId, int quantity);
    BigDecimal getPrice(String productId);
    void restoreInventory(String productId, int quantity);
}
