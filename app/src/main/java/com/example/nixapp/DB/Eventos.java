package com.example.nixapp.DB;

public class Eventos {
    String nombre_evento;
    int privacidad;
    int categoria_evento;
    String fecha;
    String hora;
    String lugar;
    String descripcion;

    public Eventos(String nombre_evento, int privacidad, int categoria_evento, String fecha, String hora, String lugar, String descripcion) {
        this.nombre_evento = nombre_evento;
        this.privacidad = privacidad;
        this.categoria_evento = categoria_evento;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.descripcion = descripcion;
    }
}
