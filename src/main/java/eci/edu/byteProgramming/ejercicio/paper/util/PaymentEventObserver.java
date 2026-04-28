package eci.edu.byteProgramming.ejercicio.paper.util;

public class Notification {
    private String companyName = "ECI Payments";
    private String fromEmail = "noreply@eciPayments.com";
    
    public void sendConfirmationEmail(String customerEmail, String customerName, PaymentMethod payment) {
        System.out.println("Notification: Sending confirmation email");
        System.out.println("   To: " + customerEmail);
        System.out.println("   From: " + fromEmail);
        System.out.println("   Subject: Payment Confirmation - " + payment.getTransactionId());
        System.out.println("   Dear " + customerName + ",");
        System.out.println("   Your payment of $" + payment.getAmount() + 
                         " has been processed successfully via " + payment.getPaymentMethod());
        System.out.println("   Transaction ID: " + payment.getTransactionId());
        System.out.println("   Thank you for your purchase!");
    }   
    
    public void sendFailureNotification(PaymentMethod payment, String customerEmail) {
        System.out.println("Notification: Sending failure notification");
        System.out.println("   To: " + customerEmail);
        System.out.println("   Subject: Payment Failed - " + payment.getTransactionId());
        System.out.println("   Your payment could not be processed. Please try again.");
    }

    // Getters 
    public String getCompanyName() { return companyName; }
    public String getFromEmail() { return fromEmail; }

}
