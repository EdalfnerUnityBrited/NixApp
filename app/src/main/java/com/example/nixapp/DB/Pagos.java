package com.example.nixapp.DB;

public class Pagos {
    String paymentMethodId;
    String descripcion;
    double monto;

    public Pagos(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Pagos(String paymentMethodId, String descripcion, double monto) {
        this.paymentMethodId = paymentMethodId;
        this.descripcion = descripcion;
        this.monto = monto;
    }
}
