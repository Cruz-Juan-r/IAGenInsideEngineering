package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Observador concreto que orquesta los efectos de un pago exitoso/fallido:
 * actualiza inventario, factura y notifica al cliente.
 *
 * Aplica el patron OBSERVER. Es solo UNO de los posibles observadores;
 * podrian existir otros (auditoria, analitica, antifraude) sin tocar
 * {@link ECIPayment}.
 */
public class PaymentEventObserver implements PaymentObserver {

    private final Inventory inventory;
    private final Facturation facturation;
    private final Notification notification;

    public PaymentEventObserver(Inventory inventory, Facturation facturation, Notification notification) {
        this.inventory = inventory;
        this.facturation = facturation;
        this.notification = notification;
    }

    @Override
    public void onPaymentSuccess(PaymentMethod payment, String customerName,
                                 String customerEmail, String productId) {
        System.out.println("\nPayment Observer: Processing successful payment events...");

        Product product = inventory.getProduct(productId);
        if (product != null) {
            inventory.discountProduct(productId, 1);
        }

        String productDetails = product != null ? product.getName() : "Product";
        facturation.generateInvoice(payment, customerName, productDetails);

        notification.sendConfirmationEmail(customerEmail, customerName, payment);

        System.out.println("All post-payment processes completed successfully!\n");
    }

    @Override
    public void onPaymentFailed(PaymentMethod payment, String customerEmail) {
        System.out.println("\nPayment Observer: Processing failed payment events...");
        notification.sendFailureNotification(payment, customerEmail);
        System.out.println("Failed payment processes completed.\n");
    }
}
