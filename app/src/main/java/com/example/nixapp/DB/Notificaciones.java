package com.example.nixapp.DB;

public class Notificaciones {
    String contenido;
    int  tipoNotificacion, id_evento;

    public Notificaciones(String contenido, int tipoNotificacion, int id_evento) {
        this.contenido = contenido;
        this.tipoNotificacion = tipoNotificacion;
        this.id_evento = id_evento;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(int tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }
}
