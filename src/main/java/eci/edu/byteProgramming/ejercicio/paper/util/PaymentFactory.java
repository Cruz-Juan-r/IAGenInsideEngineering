package eci.edu.byteProgramming.ejercicio.paper.util;

public class Product {
    private String productId;
    private String name;
    private double price;
    private String category;
    
    public Product(String productId, String name, double price, String category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
    }
    
    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
}
