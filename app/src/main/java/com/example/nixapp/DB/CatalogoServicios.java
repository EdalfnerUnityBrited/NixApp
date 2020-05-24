package com.example.nixapp.DB;

public class CatalogoServicios {
    String nombre, direccion, telefono, horarioApertura, horarioCierre, calificacion, nombreProveedor;
    int categoriaevento, lunes, martes, miercoles, jueves, viernes, sabado, domingo, id, id_usuario;

    public CatalogoServicios(String nombre, String direccion, String telefono, String horarioApertura, String horarioCierre, String calificacion, int categoriaevento, int lunes, int martes, int miercoles, int jueves, int viernes, int sabado, int domingo, String nombreProveedor) {
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
        this.nombreProveedor=nombreProveedor;
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

    public CatalogoServicios(String nombre, String direccion, String telefono, String horarioApertura, String horarioCierre, String calificacion, String nombreProveedor, int categoriaevento, int lunes, int martes, int miercoles, int jueves, int viernes, int sabado, int domingo, int id, int id_usuario) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarioApertura = horarioApertura;
        this.horarioCierre = horarioCierre;
        this.calificacion = calificacion;
        this.nombreProveedor = nombreProveedor;
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

    public CatalogoServicios(String nombre, String direccion, String telefono, String horarioApertura, String horarioCierre, String nombreProveedor, int categoriaevento, int lunes, int martes, int miercoles, int jueves, int viernes, int sabado, int domingo, int id) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarioApertura = horarioApertura;
        this.horarioCierre = horarioCierre;
        this.nombreProveedor = nombreProveedor;
        this.categoriaevento = categoriaevento;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
        this.id = id;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorarioApertura() {
        return horarioApertura;
    }

    public void setHorarioApertura(String horarioApertura) {
        this.horarioApertura = horarioApertura;
    }

    public String getHorarioCierre() {
        return horarioCierre;
    }

    public void setHorarioCierre(String horarioCierre) {
        this.horarioCierre = horarioCierre;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public int getCategoriaevento() {
        return categoriaevento;
    }

    public void setCategoriaevento(int categoriaevento) {
        this.categoriaevento = categoriaevento;
    }

    public int getLunes() {
        return lunes;
    }

    public void setLunes(int lunes) {
        this.lunes = lunes;
    }

    public int getMartes() {
        return martes;
    }

    public void setMartes(int martes) {
        this.martes = martes;
    }

    public int getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(int miercoles) {
        this.miercoles = miercoles;
    }

    public int getJueves() {
        return jueves;
    }

    public void setJueves(int jueves) {
        this.jueves = jueves;
    }

    public int getViernes() {
        return viernes;
    }

    public void setViernes(int viernes) {
        this.viernes = viernes;
    }

    public int getSabado() {
        return sabado;
    }

    public void setSabado(int sabado) {
        this.sabado = sabado;
    }

    public int getDomingo() {
        return domingo;
    }

    public void setDomingo(int domingo) {
        this.domingo = domingo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
