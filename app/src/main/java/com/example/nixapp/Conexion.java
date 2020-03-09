package com.example.nixapp;

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
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            conexion= (Connection) DriverManager.getConnection("jdbc:mysql://:3306/nix","root","d4c3b2a11313");
            //conexion= (Connection) DriverManager.getConnection("jdbc:mysql://192.168.1.74:3306/conarm","root","d4c3b2a11313");
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return conexion;
    }

    public ResultSet llenadoArma(String sql) {
        if(conexion == null){
            connectionBD();
        }
        try{
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){ex.printStackTrace();}
        System.out.println(sql);
        return null;
    }

    public ResultSet llenadoCortasServicio(String sql) {
        if(conexion == null){
            connectionBD();
        }
        try{
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){ex.printStackTrace();}
        System.out.println(sql);
        return null;
    }

    public boolean validarUsuarios(String user, String pass) {
        String sql = "SELECT * FROM validar  WHERE Usuario= '"+user+"' AND contraseña= '"+ pass+"' AND Observaciones IN ('ADMINISTRADOR','DEPOSITO','USUARIO','PORTACIONES','MUNICIPIOS','CONSULTA') AND Estatus like 'ACTIVO'";
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

    public ResultSet filtro(String sql3) {
        if(conexion == null){
            connectionBD();
        }
        try{
            ps = conexion.prepareStatement(sql3);
            rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){ex.printStackTrace();}
        System.out.println(sql3);
        return null;
    }

    public ResultSet mostrarDatos(String sql) {
        if(conexion == null){
            connectionBD();
        }
        try{
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){ex.printStackTrace();}
        System.out.println(sql);
        return null;
    }
    public int actualizarFolio(String sqlF, byte[] Scann){

        int rsu = 0;
        if(conexion == null){
            connectionBD();
        }
        try{
            ps = conexion.prepareStatement(sqlF);
            //ps.setBinaryStream(1, Scann);
            ps.setBytes(1, Scann);
            rsu = ps.executeUpdate();
        }catch(SQLException ex){ex.printStackTrace();}
        System.out.println(rsu);
        return rsu;
    }

    /*public Image getFotoArma(String matricula, String marca) {
        String sql = "select AIArma from armi where AISerie like ? and AIMarca like ?";
        Image ii = null;
        InputStream is = null;
        if(conexion == null){
            connectionBD();
        }
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, matricula);
            ps.setString(2, marca);
            rs = ps.executeQuery();
            if (rs.next()) {
                is = rs.getBinaryStream(1);
                /*if (is != null) {
                    BufferedImage bi = ImageIO.read(is);
                    ii = new CustomImageIcon(bi);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ii;
    }*/

   /* public static boolean existeSerieArma(String Serie,String Marca){
        String sql = "select ASerie from armamento where ASerie like ? and AMarca like ? ";
        boolean SerieEncontrado = false;
        if(cn == null)
            connectionBD();
        try{
            ps = cn.prepareStatement(sql);
            ps.setString(1, Serie);
            ps.setString(2, Marca);
            rs = ps.executeQuery();
            if(rs.next())
            {
                SerieEncontrado = true;
            }
        }catch(SQLException ex){ex.printStackTrace();}
        System.out.println(sql);
        return SerieEncontrado;
    }*/

   /* public static Connection Statement() {
        if(conexion == null)
            connectionBD();
        try{
            conexion.createStatement();
            return conexion;
        }catch(SQLException ex){ex.printStackTrace();}
        System.out.println(conexion);
        return null;
    }*/

}
