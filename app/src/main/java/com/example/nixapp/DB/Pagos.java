package com.example.nixapp.DB;

public class Pagos {
    String paymentMethodId;
    String descripcion;
    int monto;

    public Pagos(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Pagos(String paymentMethodId, String descripcion, int monto) {
        this.paymentMethodId = paymentMethodId;
        this.descripcion = descripcion;
        this.monto = monto;
    }
}
