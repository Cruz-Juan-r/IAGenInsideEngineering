package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.Map;

/**
 * Fabrica concreta para {@link CreditCardPayment}.
 *
 * Detalles esperados en el mapa:
 *   - "number"         (String)
 *   - "name"           (String)
 *   - "expirationDate" (String, formato MM/YY)
 *   - "cvv"            (String)
 *   - "address"        (String)
 */
public class CreditCardPaymentFactory implements PaymentFactory {

    @Override
    public PaymentMethod createPaymentMethod(double amount,
                                             String customerId,
                                             String description,
                                             Map<String, Object> details) {
        String number         = str(details, "number");
        String name           = str(details, "name");
        String expirationDate = str(details, "expirationDate");
        String cvv            = str(details, "cvv");
        String address        = str(details, "address");

        return new CreditCardPayment(amount, customerId, description,
                number, name, expirationDate, cvv, address);
    }

    private String str(Map<String, Object> details, String key) {
        if (details == null) return null;
        Object v = details.get(key);
        return v == null ? null : v.toString();
    }
}
