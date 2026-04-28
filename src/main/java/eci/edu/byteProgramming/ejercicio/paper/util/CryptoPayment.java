package eci.edu.byteProgramming.ejercicio.paper.util;

public interface PaymentObserver {
    void onPaymentSuccess(PaymentMethod payment, String customerName, String customerEmail, String productId);
    void onPaymentFailed(PaymentMethod payment, String customerEmail);
}
