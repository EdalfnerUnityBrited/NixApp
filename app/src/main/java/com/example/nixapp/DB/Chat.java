package com.example.nixapp.DB;

import java.io.Serializable;

public class Chat implements Serializable {
    public int id;
    public String name;
    public String email;

    public String id_catalogo;
    public String metodo_pago;

    public Chat(String id_catalogo) {
        this.id_catalogo = id_catalogo;
    }

    public Chat(int id){

    }

    public Chat(String id_catalogo, String metodo_pago) {
        this.id_catalogo = id_catalogo;
        this.metodo_pago = metodo_pago;
    }

    public Chat(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
