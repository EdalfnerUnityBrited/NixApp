package com.example.nixapp.DB;

public class Citas {
    String hora, fecha;
    int id_servicio, id_evento;

    public Citas(String hora, String fecha, int id_servicio, int id_evento) {
        this.hora = hora;
        this.fecha = fecha;
        this.id_servicio = id_servicio;
        this.id_evento = id_evento;
    }
    
}
