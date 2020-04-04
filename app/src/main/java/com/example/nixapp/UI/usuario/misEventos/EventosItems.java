package com.example.nixapp.UI.usuario.misEventos;

public class EventosItems {
    private String mEventName;
    private int mImage;

    public EventosItems(String EventName, int Image) {
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