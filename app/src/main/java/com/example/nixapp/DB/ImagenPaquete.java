package com.example.nixapp.DB;

public class ImagenPaquete {
    String imagen;
    int id_paquete, id;

    public ImagenPaquete(String imagen, int id_paquete) {
        this.imagen = imagen;
        this.id_paquete = id_paquete;
    }

    public ImagenPaquete(String imagen, int id_paquete, int id) {
        this.imagen = imagen;
        this.id_paquete = id_paquete;
        this.id = id;
    }
}
