package com.example.nixapp.DB;

public class CotizacionArticulo {
    String cantidad, id;
    int id_cotizacion, id_articulo;

    public String getCantidad() {
        return cantidad;
    }

    public String getId() {
        return id;
    }

    public int getId_cotizacion() {
        return id_cotizacion;
    }

    public int getId_articulo() {
        return id_articulo;
    }

    public CotizacionArticulo(String cantidad, int id_cotizacion, int id_articulo) {
        this.cantidad = cantidad;
        this.id_cotizacion = id_cotizacion;
        this.id_articulo = id_articulo;
    }

    public CotizacionArticulo(String cantidad, String id, int id_cotizacion, int id_articulo) {
        this.cantidad = cantidad;
        this.id = id;
        this.id_cotizacion = id_cotizacion;
        this.id_articulo = id_articulo;
    }
}
