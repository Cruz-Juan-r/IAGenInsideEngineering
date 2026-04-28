package eci.edu.byteProgramming.ejercicio.paper.util;

public enum PaymentStatus {

    PENDING("Pendiente"),
    PROCESSING("Procesando"),
    COMPLETED("Completado"),
    FAILED("Fallido"),
    CANCELED("Cancelado");
    
    private final String name;


    PaymentStatus(String name) {
        this.name = name;
    } 
   
    
    public String getName() { return name; }

}
