package com.example.nixapp.DB;

public class Citas {
    String hora;
    String fecha;
    String nombre;
    String nombre_evento;
    int id_servicio, id_evento;

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public Citas(String hora, String fecha, String nombre, String nombre_evento) {
        this.hora = hora;
        this.fecha = fecha;
        this.nombre = nombre;
        this.nombre_evento = nombre_evento;
    }

    public Citas(String hora, String fecha, int id_servicio, int id_evento) {
        this.hora = hora;
        this.fecha = fecha;
        this.id_servicio = id_servicio;
        this.id_evento = id_evento;
    }
    
}
