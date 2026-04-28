package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Subject del patron OBSERVER y cliente del FACTORY METHOD.
 *
 * - Mantiene una lista de {@link PaymentObserver} y los notifica cuando un pago
 *   termina (exito o fallo). Permite agregar nuevos observadores (auditoria,
 *   analitica, antifraude, etc.) sin tocar esta clase -> OCP.
 * - Recibe una {@link PaymentFactory} desde afuera, asi que no conoce las
 *   clases concretas de PaymentMethod -> DIP.
 */
public class ECIPayment {

    private final List<PaymentObserver> observers = new ArrayList<>();

    public void addObserver(PaymentObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PaymentObserver observer) {
        observers.remove(observer);
    }

    public int getObserverCount() {
        return observers.size();
    }

    public boolean processPayment(PaymentFactory factory,
                                  double amount,
                                  String customerId,
                                  String description,
                                  String customerName,
                                  String customerEmail,
                                  String productId,
                                  Map<String, Object> paymentDetails) {

        System.out.println("ECI Payments: Starting payment process...");
        System.out.println("Customer: " + customerName + " (" + customerEmail + ")");
        System.out.println("Amount: $" + amount);
        System.out.println("Description: " + description);
        System.out.println("----------------------------------------");

        PaymentMethod payment = factory.createPaymentMethod(amount, customerId, description, paymentDetails);
        boolean success = payment.processPayment();

        if (success) {
            System.out.println("Payment processed successfully!");
            notifyPaymentSuccess(payment, customerName, customerEmail, productId);
        } else {
            System.out.println("Payment failed!");
            notifyPaymentFailed(payment, customerEmail);
        }

        return success;
    }

    private void notifyPaymentSuccess(PaymentMethod payment, String customerName,
                                      String customerEmail, String productId) {
        for (PaymentObserver observer : observers) {
            observer.onPaymentSuccess(payment, customerName, customerEmail, productId);
        }
    }

    private void notifyPaymentFailed(PaymentMethod payment, String customerEmail) {
        for (PaymentObserver observer : observers) {
            observer.onPaymentFailed(payment, customerEmail);
        }
    }
}
