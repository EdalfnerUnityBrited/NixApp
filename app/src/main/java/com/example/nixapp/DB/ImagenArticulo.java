package com.example.nixapp.DB;

public class ImagenArticulo {
    String imagen;
    int id_articulo, id;

    public ImagenArticulo(String imagen, int id_articulo) {
        this.imagen = imagen;
        this.id_articulo = id_articulo;
    }

    public ImagenArticulo(String imagen, int id_articulo, int id) {
        this.imagen = imagen;
        this.id_articulo = id_articulo;
        this.id = id;
    }
}
