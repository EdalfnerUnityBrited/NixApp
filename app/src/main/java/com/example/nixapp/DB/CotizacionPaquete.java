package com.example.nixapp.DB;

public class CotizacionPaquete {
    String cantidad, id;
    int id_cotizacion, id_paquete;

    public CotizacionPaquete(String cantidad, int id_cotizacion, int id_paquete) {
        this.cantidad = cantidad;
        this.id_cotizacion = id_cotizacion;
        this.id_paquete = id_paquete;
    }

    public CotizacionPaquete(String cantidad, String id, int id_cotizacion, int id_paquete) {
        this.cantidad = cantidad;
        this.id = id;
        this.id_cotizacion = id_cotizacion;
        this.id_paquete = id_paquete;
    }
}
