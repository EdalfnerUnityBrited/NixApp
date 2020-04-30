package com.example.nixapp.DB;

public class Prospectos {
    String id_evento;
    int id;

    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfirmacionasistencia() {
        return confirmacionasistencia;
    }

    public void setConfirmacionasistencia(int confirmacionasistencia) {
        this.confirmacionasistencia = confirmacionasistencia;
    }

    public int getId_prospecto() {
        return id_prospecto;
    }

    public void setId_prospecto(int id_prospecto) {
        this.id_prospecto = id_prospecto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    int confirmacionasistencia;
    int id_prospecto;
    String estado;

    public Prospectos(String id_evento, String estado)
    {
        this.id_evento = id_evento;
        this.estado = estado;
    }

    public Prospectos(String id_evento)
    {
        this.id_evento = id_evento;
    }

    public Prospectos(String id_evento, String estado,int id, int id_prospecto,int confirmacionasistencia)
    {
        this.id_evento = id_evento;
        this.estado = estado;
        this.id = id;
        this.confirmacionasistencia = confirmacionasistencia;
        this.id_prospecto = id_prospecto;
    }
}
