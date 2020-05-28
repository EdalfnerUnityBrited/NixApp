package com.example.nixapp.DB;

public class Contrataciones {
    String id;
    String estado_servicio;
    String fecha;
    String hora;
    String metodo_pago;
    String id_servicio;
    String id_evento;
    String nombre_evento;
    String nombre;
    String lugar;
    String desglose;

    public Contrataciones(String metodo_pago, String id_servicio, String desglose) {
        this.metodo_pago = metodo_pago;
        this.id_servicio = id_servicio;
        this.desglose = desglose;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public Contrataciones(String id, String estado_servicio, String fecha, String hora, String metodo_pago, String id_servicio, String id_evento, String nombre_evento,String nombre,String lugar) {
        this.id = id;
        this.estado_servicio = estado_servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.metodo_pago = metodo_pago;
        this.id_servicio = id_servicio;
        this.id_evento = id_evento;
        this.nombre_evento = nombre_evento;
        this.nombre = nombre;
        this.lugar = lugar;
    }

    public Contrataciones(String id, String estado_servicio, String fecha, String hora, String metodo_pago, String id_servicio, String id_evento, String nombre_evento) {
        this.id = id;
        this.estado_servicio = estado_servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.metodo_pago = metodo_pago;
        this.id_servicio = id_servicio;
        this.id_evento = id_evento;
        this.nombre_evento = nombre_evento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstado_servicio() {
        return estado_servicio;
    }

    public void setEstado_servicio(String estado_servicio) {
        this.estado_servicio = estado_servicio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public String getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(String id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }
}
