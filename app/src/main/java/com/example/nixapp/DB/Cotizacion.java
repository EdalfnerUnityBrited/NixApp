package com.example.nixapp.DB;

public class Cotizacion {
    String total, estado, id;
    int id_servicio, id_evento;


    public Cotizacion(String total, int id_servicio, int id_evento) {
        this.total = total;
        this.id_servicio = id_servicio;
        this.id_evento = id_evento;
    }

    public Cotizacion(String total, String estado, String id, int id_servicio, int id_evento) {
        this.total = total;
        this.estado = estado;
        this.id = id;
        this.id_servicio = id_servicio;
        this.id_evento = id_evento;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }
}
