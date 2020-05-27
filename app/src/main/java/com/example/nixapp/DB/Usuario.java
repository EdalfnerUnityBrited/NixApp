package com.example.nixapp.DB;

import java.io.Serializable;

public class Usuario implements Serializable {
    public int id;
    private String password_confirmation;
    public int tipoUsuario;
    public String name;
    public String apellidoP;
    public String apellidoM;
    public String email;
    public String fechaNac;
    private String password;
    public String telefono;
    private int calificacion;
    public String fotoPerfil;


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



    public Usuario(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Usuario(String name, String apellidoP, String apellidoM, String fechaNac, String telefono) {
        this.name = name;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.fechaNac = fechaNac;
        this.telefono = telefono;
    }

    public Usuario(String password_confirmation, String name, String password) {
        this.password_confirmation = password_confirmation;
        this.name = name;
        this.password = password;
    }
}
