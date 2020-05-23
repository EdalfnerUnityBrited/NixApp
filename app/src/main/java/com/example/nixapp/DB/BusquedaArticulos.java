package com.example.nixapp.DB;

public class BusquedaArticulos {
    String nombre, precioPor, precioIni, precioFin;
    int categoria;

    public BusquedaArticulos(String nombre, String precioPor, String precioIni, String precioFin, int categoria) {
        this.nombre = nombre;
        this.precioPor = precioPor;
        this.precioIni = precioIni;
        this.precioFin = precioFin;
        this.categoria = categoria;
    }
}
