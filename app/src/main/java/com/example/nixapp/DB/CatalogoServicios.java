package com.example.nixapp.DB;

public class CatalogoServicios {
    String nombre, direccion, telefono, horarioApertura, horarioCierre, calificacion;
    int categoriaevento, lunes, martes, miercoles, jueves, viernes, sabado, domingo, id, id_usuario;

    public CatalogoServicios(String nombre, String direccion, String telefono, String horarioApertura, String horarioCierre, String calificacion, int categoriaevento, int lunes, int martes, int miercoles, int jueves, int viernes, int sabado, int domingo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarioApertura = horarioApertura;
        this.horarioCierre = horarioCierre;
        this.calificacion = calificacion;
        this.categoriaevento = categoriaevento;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
    }

    public CatalogoServicios(String nombre, String direccion, String telefono, String horarioApertura, String horarioCierre, String calificacion, int categoriaevento, int lunes, int martes, int miercoles, int jueves, int viernes, int sabado, int domingo, int id, int id_usuario) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarioApertura = horarioApertura;
        this.horarioCierre = horarioCierre;
        this.calificacion = calificacion;
        this.categoriaevento = categoriaevento;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
        this.id = id;
        this.id_usuario = id_usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
