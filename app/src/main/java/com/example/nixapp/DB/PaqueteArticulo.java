package com.example.nixapp.DB;

public class PaqueteArticulo {
    int id_paquete;

    public String getArticulo() {
        return articulo;
    }

    String articulo;

    public PaqueteArticulo(int id_paquete, String articulo) {
        this.id_paquete = id_paquete;
        this.articulo = articulo;
    }

}
