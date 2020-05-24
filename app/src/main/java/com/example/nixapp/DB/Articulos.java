package com.example.nixapp.DB;

public class Articulos {
    String nombre, descripcion, precioPor, precio;
    int categoria_articulo, id_catalogoServicio, id;

    public Articulos(String nombre, String descripcion, String precioPor, String precio, int categoria_articulo, int id_catalogoServicio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioPor = precioPor;
        this.precio = precio;
        this.categoria_articulo = categoria_articulo;
        this.id_catalogoServicio = id_catalogoServicio;
    }

    public Articulos(String nombre, String descripcion, String precioPor, String precio, int categoria_articulo, int id_catalogoServicio, int id) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioPor = precioPor;
        this.precio = precio;
        this.categoria_articulo = categoria_articulo;
        this.id_catalogoServicio = id_catalogoServicio;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Articulos(int id) {
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

    public String getPrecioPor() {
        return precioPor;
    }

    public void setPrecioPor(String precioPor) {
        this.precioPor = precioPor;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getCategoria_articulo() {
        return categoria_articulo;
    }

    public void setCategoria_articulo(int categoria_articulo) {
        this.categoria_articulo = categoria_articulo;
    }

    public int getId_catalogoServicio() {
        return id_catalogoServicio;
    }

    public void setId_catalogoServicio(int id_catalogoServicio) {
        this.id_catalogoServicio = id_catalogoServicio;
    }
}
