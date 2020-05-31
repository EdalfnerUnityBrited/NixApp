package com.example.nixapp.DB;

public class Calificacion {
    String id_servicio, calificacion;

    public Calificacion(String id_servicio, String calificacion) {
        this.id_servicio = id_servicio;
        this.calificacion = calificacion;
    }

    public String getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(String id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }
}
