package eci.edu.byteProgramming.ejercicio.paper.util;

public class Facturation {
    private String companyName = "ECI Payments Corp";
    private String taxId = "NIT 900123456-1";
    private String address = "Calle 100 #45-30, Bogotá D.C.";
    private String city = "Bogotá D.C.";
    private String country = "Colombia";
    private double taxRate = 0.19; // 19% IVA
    private int lastInvoiceNumber = 1000;
    private String currency = "COP";
    
    public void generateInvoice(PaymentMethod payment, String customerName, String productDetails) {
        String invoiceNumber = "INV-" + (++lastInvoiceNumber);
        double subtotal = payment.getAmount();
        double taxAmount = subtotal * taxRate;
        double totalAmount = subtotal + taxAmount;
        
        System.out.println("Facturation: Invoice generated");
        System.out.println("   Invoice Number: " + invoiceNumber);
        System.out.println("   Company: " + companyName + " (" + taxId + ")");
        System.out.println("   Customer: " + customerName + " (ID: " + payment.getCustomerId() + ")");
        System.out.println("   Product: " + productDetails);
        System.out.println("   Subtotal: $" + String.format("%.2f", subtotal) + " " + currency);
        System.out.println("   Tax (19%): $" + String.format("%.2f", taxAmount) + " " + currency);
        System.out.println("   Total: $" + String.format("%.2f", totalAmount) + " " + currency);
        System.out.println("   Transaction ID: " + payment.getTransactionId());
        System.out.println("   Date: " + payment.getTimestamp());
        System.out.println("   Payment Method: " + payment.getPaymentMethod());
        System.out.println("   Address: " + address + ", " + city + ", " + country);
        System.out.println("   ----------------------------------------");
    }
    
    // Métodos auxiliares
    public double calculateTax(double amount) {
        return amount * taxRate;
    }
    
    public double calculateTotal(double subtotal) {
        return subtotal + calculateTax(subtotal);
    }
    
    public String getNextInvoiceNumber() {
        return "INV-" + (lastInvoiceNumber + 1);
    }
    
    // Getters para configuración
    public String getCompanyName() { return companyName; }
    public String getTaxId() { return taxId; }
    public double getTaxRate() { return taxRate; }
    public String getCurrency() { return currency; }
    
    // Setters para configuración
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    
}
