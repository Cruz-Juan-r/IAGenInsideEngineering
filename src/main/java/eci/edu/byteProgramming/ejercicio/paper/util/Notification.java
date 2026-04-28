package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.Date;

/**
 * Plantilla base de un metodo de pago.
 *
 * Aplica:
 * - ENCAPSULAMIENTO: los atributos son protected y se exponen con getters.
 * - POLIMORFISMO: los metodos abstractos {@link #processPayment()} y
 *   {@link #getPaymentMethod()} se resuelven en runtime segun la subclase.
 * - Implementa {@link ValidatePayment} para forzar a cada metodo a validar.
 */
public abstract class PaymentMethod implements ValidatePayment {

    protected double amount;
    protected String transactionID;
    protected String customerID;
    protected String currency;
    protected Date timestamp;
    protected PaymentStatus status;
    protected String description;

    protected PaymentMethod(double amount, String customerID, String description) {
        this.amount = amount;
        this.customerID = customerID;
        this.description = description;
        this.currency = "USD";
        this.status = PaymentStatus.PENDING;
        this.timestamp = new Date();
        this.transactionID = generateTransactionIdWithPrefix(getPaymentMethod());
    }

    public abstract boolean processPayment();
    public abstract String getPaymentMethod();

    protected final String generateTransactionIdWithPrefix(String paymentType) {
        String prefix = getPaymentTypePrefix(paymentType);
        long ts = System.currentTimeMillis();
        int random = (int) (Math.random() * 9999);
        return String.format("%s%d%04d", prefix, ts, random);
    }

    private String getPaymentTypePrefix(String paymentType) {
        if (paymentType == null) return "TX";
        switch (paymentType) {
            case "CREDIT_CARD":   return "CC";
            case "PAYPAL":        return "PP";
            case "CRYPTOCURRENCY":
            case "CRYPTO":        return "CR";
            default:              return "TX";
        }
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount()           { return amount; }
    public String getTransactionId()    { return transactionID; }
    public PaymentStatus getStatus()    { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public String getCustomerId()       { return customerID; }
    public String getDescription()      { return description; }
    public Date getTimestamp()          { return timestamp; }
    public String getCurrency()         { return currency; }
}
