package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Producto concreto: pago con PayPal.
 */
public class PaypalPayment extends PaymentMethod {

    private final String email;
    private String paypalTransactionId;
    private final String authToken;

    public PaypalPayment(double amount, String customerId, String description,
                         String email, String authToken) {
        super(amount, customerId, description);
        this.email = email;
        this.authToken = authToken;
    }

    @Override
    public boolean validatePaymentMethod() {
        return validateEmail() && validateAuthToken();
    }

    private boolean validateEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean validateAuthToken() {
        return authToken != null && authToken.length() > 10;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing PayPal payment...");

        if (!validatePaymentMethod()) {
            System.out.println("PayPal validation failed!");
            setStatus(PaymentStatus.FAILED);
            return false;
        }

        setStatus(PaymentStatus.PROCESSING);

        try {
            this.paypalTransactionId = "PP" + System.currentTimeMillis();
            System.out.println("PayPal payment authorized for: " + email);
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "PAYPAL";
    }

    public String getEmail()                { return email; }
    public String getPaypalTransactionId()  { return paypalTransactionId; }
}
