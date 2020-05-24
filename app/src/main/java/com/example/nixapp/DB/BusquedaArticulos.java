package com.example.nixapp.DB;

public class BusquedaArticulos {
    String nombre, precioPor, precioIni, precioFin;
    int categoria;

    public BusquedaArticulos(String nombre, String precioIni, String precioFin, int categoria) {
        this.nombre = nombre;
        this.precioIni = precioIni;
        this.precioFin = precioFin;
        this.categoria = categoria;
    }
}
