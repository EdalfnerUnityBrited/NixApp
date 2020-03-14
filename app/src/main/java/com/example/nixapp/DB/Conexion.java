package com.example.nixapp.DB;

import android.os.StrictMode;

import com.mysql.jdbc.Connection;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    public Connection conexion = null;
    private static Statement st = null;
    private static PreparedStatement ps = null;
    //private static final createStatement cs = null;
    private static ResultSet rs = null;

    public Connection connectionBD() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://34.94.92.8:3306/nixdb", "neriadmin", "1234");
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return conexion;
    }

    public boolean validarUsuarios(String email, String password) {
        String sql = "SELECT * FROM usuarios  WHERE email= '"+email+"' AND contraseña= '"+password+"'";
        boolean usuarioEncontrado = false;
        try{
            ps = conexion.prepareStatement(sql);
            //ps.setString(1, email);
            rs = ps.executeQuery();
            if(rs.first())
                return true;        //usuario validado correctamente
            else
                return false;
        }catch(SQLException ex){ex.printStackTrace();}
        return usuarioEncontrado;
    }


    public boolean validarEmail(String email) {
        if(conexion == null){
            connectionBD();
        }
        String sqlnueva = "SELECT * FROM usuarios  WHERE email= '"+email+"'";
        boolean emailEncontrado = false;
        try{
            ps = conexion.prepareStatement(sqlnueva);
            //ps.setString(1, email);
            rs = ps.executeQuery();
            if(rs.first())
                return true;        //usuario validado correctamente
            else
                return false;
        }catch(SQLException ex){ex.printStackTrace();}
        return emailEncontrado;
    }

    public boolean validarTelefono(String telefono) {
        if(conexion == null){
            connectionBD();
        }
        String sqlnueva = "SELECT * FROM usuarios  WHERE telefono= '"+telefono+"'";
        boolean telefonoEncontrado = false;
        try{
            ps = conexion.prepareStatement(sqlnueva);
            //ps.setString(1, email);
            rs = ps.executeQuery();
            if(rs.first())
                return true;        //usuario validado correctamente
            else
                return false;
        }catch(SQLException ex){ex.printStackTrace();}
        return telefonoEncontrado;
    }
}