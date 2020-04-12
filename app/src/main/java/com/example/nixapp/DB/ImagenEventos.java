package com.example.nixapp.DB;

public class ImagenEventos {
    String imagen;
    String id_evento;

    public ImagenEventos(String imagen, String id_evento) {
        this.imagen = imagen;
        this.id_evento = id_evento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
