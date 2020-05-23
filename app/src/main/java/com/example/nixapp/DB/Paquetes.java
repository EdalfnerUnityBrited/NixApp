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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFotoPaquete() {
        return fotoPaquete;
    }

    public void setFotoPaquete(String fotoPaquete) {
        this.fotoPaquete = fotoPaquete;
    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }
}
