package com.example.nixapp.UI.usuario.misEventos.BusquedaServicios;

public class ServiciosItems {
    private String mEventName;
    private int mImage;

    public ServiciosItems(String EventName, int Image) {
        mEventName = EventName;
        mImage = Image;
    }

    public String getEventoName() {
        return mEventName;
    }

    public int getImages() {
        return mImage;
    }
}