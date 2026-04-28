package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.Map;

/**
 * Fabrica concreta para {@link PaypalPayment}.
 *
 * Detalles esperados en el mapa:
 *   - "email"     (String)
 *   - "authToken" (String)
 */
public class PaypalPaymentFactory implements PaymentFactory {

    @Override
    public PaymentMethod createPaymentMethod(double amount,
                                             String customerId,
                                             String description,
                                             Map<String, Object> details) {
        String email     = str(details, "email");
        String authToken = str(details, "authToken");

        return new PaypalPayment(amount, customerId, description, email, authToken);
    }

    private String str(Map<String, Object> details, String key) {
        if (details == null) return null;
        Object v = details.get(key);
        return v == null ? null : v.toString();
    }
}
