package com.example.nixapp.DB;

public class Paquetes {
    String nombre, descripcion, precio, fotoPaquete;
    int id_servicio, id;

    public Paquetes(String nombre, String descripcion, String precio, String fotoPaquete, int id_servicio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fotoPaquete = fotoPaquete;
        this.id_servicio = id_servicio;
    }

    public Paquetes(String nombre, String descripcion, String precio, String fotoPaquete, int id_servicio, int id) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fotoPaquete = fotoPaquete;
        this.id_servicio = id_servicio;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
