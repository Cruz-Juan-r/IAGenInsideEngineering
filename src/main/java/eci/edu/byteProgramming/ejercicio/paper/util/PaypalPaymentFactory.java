package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private static final String LAPTOP001 = "LAPTOP001";
    private Map<String, Product> products;
    private Map<String, Integer> stock;
    
    public Inventory() {
        this.products = new HashMap<>();
        this.stock = new HashMap<>();
        initializeInventory();
    }
    
    private void initializeInventory() {
        products.put(LAPTOP001, new Product(LAPTOP001, "Gaming Laptop", 1200.00, "Electronics"));
        products.put("PHONE001", new Product("PHONE001", "Smartphone", 800.00, "Electronics"));
        products.put("BOOK001", new Product("BOOK001", "Java Programming Book", 45.99, "Books"));

        stock.put(LAPTOP001, 5);
        stock.put("PHONE001", 10);
        stock.put("BOOK001", 20);
    }
    
    public boolean discountProduct(String productId, int quantity) {
        if (stock.containsKey(productId) && stock.get(productId) >= quantity) {
            int currentStock = stock.get(productId);
            stock.put(productId, currentStock - quantity);
            System.out.println("✅ Inventory: Discounted " + quantity + " units of " + 
                             products.get(productId).getName());
            System.out.println("   Remaining stock: " + stock.get(productId));
            return true;
        } else {
            System.out.println("Inventory: Insufficient stock for " + productId);
            return false;
        }
    }
    
    public Product getProduct(String productId) {
        return products.get(productId);
    }
    
    public int getStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }
}

