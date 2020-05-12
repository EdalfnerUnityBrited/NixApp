package com.example.nixapp.DB;

public class Busqueda {
    String municipio, fechaIni, fechaFin, cupo, cover, nombre;
    int categoria;

    public Busqueda(String municipio, String fechaIni, String fechaFin, String cupo, String cover, int categoria) {
        this.municipio = municipio;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.cupo = cupo;
        this.cover = cover;
        this.categoria = categoria;
    }

    public Busqueda(String municipio, String fechaIni, String fechaFin, String cupo, String cover, String nombre, int categoria) {
        this.municipio = municipio;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.cupo = cupo;
        this.cover = cover;
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public Busqueda(String cupo) {
        this.cupo = cupo;
    }

    public Busqueda(String cupo, String cover) {
        this.cupo = cupo;
        this.cover = cover;
    }
}
