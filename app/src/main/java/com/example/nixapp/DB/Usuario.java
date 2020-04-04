package com.example.nixapp.DB;

import java.io.Serializable;

public class Usuario implements Serializable {
    public int id;
    private String password_confirmation;
    private int tipoUsuario;
    public String name;
    private String apellidoP;
    private String apellidoM;
    public String email;
    private String fechaNac;
    private String password;
    private String telefono;
    private int calificacion;
    private String fotoPerfil;

    public Usuario(int tipoUsuario, String nname, String apellidoP, String apellidoM, String email, String fechaNac, String contras, String telefono, int calificacion, String fotoPerfil, String password_confirmation) {
        this.tipoUsuario = tipoUsuario;
        this.name = nname;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.email = email;
        this.fechaNac = fechaNac;
        this.password = contras;
        this.telefono = telefono;
        this.calificacion = calificacion;
        this.fotoPerfil = fotoPerfil;
        this.password_confirmation=password_confirmation;
    }

    public Usuario() {

    }

    public Usuario(String email, String password) {

        this.email = email;
        this.password = password;
    }
}
