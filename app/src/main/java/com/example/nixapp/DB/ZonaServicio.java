package com.example.nixapp.DB;

public class ZonaServicio {
    int id, id_catalogoServicio;
    String municipio;

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public ZonaServicio(int id_catalogoServicio, String municipio) {
        this.id_catalogoServicio = id_catalogoServicio;
        this.municipio = municipio;
    }

}
