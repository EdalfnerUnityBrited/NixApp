package com.example.nixapp.UI.serviciosContratados;

class Chats {
    public int id_usuario, id_proveedor;
    public Chats(){

    }

    public Chats(int id_usuario, int id_proveedor) {
        this.id_usuario = id_usuario;
        this.id_proveedor = id_proveedor;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }
}
