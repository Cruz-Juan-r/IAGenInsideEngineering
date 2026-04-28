package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Producto concreto: pago con tarjeta de credito.
 * Conoce su propia logica de validacion y procesamiento (Single Responsibility).
 */
public class CreditCardPayment extends PaymentMethod {

    private final String number;
    private final String name;
    private final String expirationDate;
    private final String cvv;
    private final String cardType;
    private final String address;

    public CreditCardPayment(double amount, String customerID, String description,
                             String number, String name, String expirationDate,
                             String cvv, String address) {
        super(amount, customerID, description);
        this.number = number;
        this.name = name;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.address = address;
        this.cardType = determineCardType(number);
    }

    @Override
    public boolean validatePaymentMethod() {
        return validateCardNumber() && validateCVV() && validateExpirationDate();
    }

    private boolean validateCardNumber() {
        return number != null && number.length() >= 13 && number.length() <= 19;
    }

    private boolean validateCVV() {
        return cvv != null && cvv.length() >= 3 && cvv.length() <= 4;
    }

    private boolean validateExpirationDate() {
        // Formato MM/YY
        return expirationDate != null && expirationDate.matches("\\d{2}/\\d{2}");
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing Credit Card payment...");

        if (!validatePaymentMethod()) {
            System.out.println("Credit Card validation failed!");
            setStatus(PaymentStatus.FAILED);
            return false;
        }

        setStatus(PaymentStatus.PROCESSING);

        try {
            System.out.println("Contacting bank for card: " + maskCardNumber());
            System.out.println("Payment authorized by bank");
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "CREDIT_CARD";
    }

    private String determineCardType(String cardNumber) {
        if (cardNumber == null) return "UNKNOWN";
        if (cardNumber.startsWith("4")) return "VISA";
        if (cardNumber.startsWith("5")) return "MASTERCARD";
        if (cardNumber.startsWith("3")) return "AMEX";
        return "UNKNOWN";
    }

    public String maskCardNumber() {
        return "**** **** **** " + number.substring(number.length() - 4);
    }

    public String getCardHolderName() { return name; }
    public String getCardType()       { return cardType; }
    public String getAddress()        { return address; }
}
