package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.Map;

/**
 * Patron FACTORY METHOD: define la operacion de fabricacion de un PaymentMethod.
 *
 * Cada metodo de pago necesita parametros muy distintos (numero de tarjeta,
 * email, wallet, etc.), por eso la fabrica recibe un mapa de detalles propios
 * del medio de pago, mientras que los datos comunes (amount, customerId,
 * description) van como parametros explicitos.
 *
 * Aplica:
 * - DIP (SOLID): {@link ECIPayment} depende de esta abstraccion, no de
 *   las clases concretas {@link CreditCardPayment}, {@link PaypalPayment}, etc.
 * - OCP (SOLID): para soportar un nuevo metodo de pago basta con crear una
 *   nueva implementacion de PaymentFactory; ni ECIPayment ni los observers
 *   se tocan.
 */
public interface PaymentFactory {

    /**
     * @param amount      monto a pagar
     * @param customerId  id del cliente
     * @param description descripcion de la compra
     * @param details     parametros propios del metodo de pago (numero de
     *                    tarjeta, email, wallet, etc.). Puede ser nulo o vacio
     *                    si la fabrica no lo necesita.
     * @return un {@link PaymentMethod} ya configurado y validado en su construccion.
     */
    PaymentMethod createPaymentMethod(double amount,
                                      String customerId,
                                      String description,
                                      Map<String, Object> details);
}
