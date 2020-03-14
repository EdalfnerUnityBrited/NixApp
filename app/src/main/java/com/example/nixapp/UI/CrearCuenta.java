package com.example.nixapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nixapp.DB.Conexion;
import com.example.nixapp.R;

import java.util.regex.Pattern;

public class CrearCuenta extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    Switch switchProveedor;
    int proovedor=0;
    Button btnCrear;
    EditText etNombre, etApellidoP, etApellidoM, etEmail, etTelefono, etFechaNac, etPassword, etPasswordConf;
    Intent intent;

    private Conexion conne= new Conexion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        switchProveedor = (Switch) findViewById(R.id.switchProveedor);

        switchProveedor.setTextOn("Si");
        switchProveedor.setTextOff("No");
        etNombre=findViewById(R.id.nombre);
        etApellidoP=findViewById(R.id.apellido_paterno);
        etApellidoM=findViewById(R.id.apellido_materno);
        etEmail=findViewById(R.id.arroba);
        etTelefono=findViewById(R.id.telefono);
        etFechaNac=findViewById(R.id.fecha_nacimiento);
        etPasswordConf=findViewById(R.id.passwordConfirmacion);
        btnCrear=findViewById(R.id.buttonCrearCuenta);
        etPassword=findViewById(R.id.password);

        switchProveedor.setOnCheckedChangeListener(this);
        btnCrear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String email = etEmail.getText().toString();
        String password= etPassword.getText().toString();
        String nombre = etNombre.getText().toString();
        String apellidoPaterno= etApellidoP.getText().toString();
        String apellidoMaterno = etApellidoM.getText().toString();
        String telefono= etTelefono.getText().toString();
        String fechaNacimiento = etFechaNac.getText().toString();
        String passwordConfirmacion= etPasswordConf.getText().toString();
        if (email.isEmpty()){
            etEmail.setError("Ingrese Email");
        }
        if (password.isEmpty()){
            etPassword.setError("Ingrese Password");
        }
        if (nombre.isEmpty()){
            etNombre.setError("Ingrese Nombre");
        }
        if (apellidoPaterno.isEmpty()){
            etApellidoP.setError("Ingrese Apellido Paterno");
        }
        if (apellidoMaterno.isEmpty()){
            etApellidoM.setError("Ingrese Apellido Materno");
        }
        if (telefono.isEmpty()){
            etTelefono.setError("Ingrese Telefono");
        }
        if (fechaNacimiento.isEmpty()){
            etFechaNac.setError("Ingrese Cumpleaños");
        }
        if (passwordConfirmacion.isEmpty()){
            etPasswordConf.setError("Confirme la contraseña");
        }
        else{
            boolean verificarCorreo=verificarCorreo (email);
            if (!verificarCorreo){
            boolean verificarNumero= veificarNumero(telefono);
            if (!verificarNumero) {
            boolean verificarNombre= verificarNombre(nombre, apellidoMaterno, apellidoPaterno);
            if (!verificarNombre){
                boolean verificarContraseña=verificarContraseña(password,passwordConfirmacion);
                if (!verificarContraseña){
                    Toast.makeText(getApplicationContext(),"Bienvenido",
                            Toast.LENGTH_LONG).show();
                }
            }
            }
            }

        }

    }

    private boolean verificarContraseña(String password, String passwordConfirmacion) {
        boolean verificarPassword=false;
        if (password.length()>=8||password.length()>18){
            if (password.matches("[A-Za-z0-9]+") && password.length() > 2){
            if (!password.matches(passwordConfirmacion)){
                etPasswordConf.setError("Las contraseñas no coinciden");
                verificarPassword=true;
            }
            }
            else {
                etPassword.setError("La contraseña solo puede tener letras o numeros");
            }
        }
        else {
            etPassword.setError("La contraseña debe tener un minimo de 8 caracteres " +
                    "y un maximo de 18");
            verificarPassword=true;
        }

        return  verificarPassword;
    }

    private boolean verificarNombre(String nombre, String apellidoMaterno, String apellidoPaterno) {

        boolean verificacionNombre=false;

        if (nombre.matches("[A-Za-z\\s]+") && nombre.length() > 2){
            if (apellidoPaterno.matches("[A-Za-z\\s]+") && nombre.length() > 2){
                if (apellidoMaterno.matches("[A-Za-z\\s]+") && nombre.length() > 2){

                }
                else{
                    etApellidoM.setError("Solo puede insertar letras");
                    verificacionNombre=true;
                }
            }
            else{
                etApellidoP.setError("Solo puede insertar letras");
                verificacionNombre=true;
            }
        }
        else{
            etNombre.setError("Solo puede insertar letras");
            verificacionNombre=true;
        }
        return verificacionNombre;
    }

    private boolean veificarNumero(String telefono) {
        boolean verificacionTelefono=false;
        if (telefono.startsWith("33")){
            if (telefono.length()==10){
                if (telefono.matches("[0-9]+") && telefono.length() > 2) {
                    verificacionTelefono=existeTelefono(telefono);
                }
                else {
                    etTelefono.setError("Ese no es un número valido");
                    verificacionTelefono=true;
                }
            }
            else{
                etTelefono.setError("Ese no es un número valido");
                verificacionTelefono=true;
            }
        }
        else {
            etTelefono.setError("Ese no es un numero de telefono valido");
            verificacionTelefono=true;
        }
        return verificacionTelefono;
    }

    private boolean existeTelefono(String telefono) {
        boolean telefonoEncontrado=conne.validarTelefono(telefono);
        if (telefonoEncontrado==true){
            etTelefono.setError("El telefono ya está registrado");
        }
        return telefonoEncontrado;
    }

    private boolean verificarCorreo(String email) {
        boolean verificacion = false;
        if (email.contains("@hotmail.com")){
            verificacion=existeCorreo(email);
        }
        else if(email.contains("@gmail.com")){
            verificacion=existeCorreo(email);
        }
        else if(email.contains("@outlook.com")){

        }
        else if (email.contains("@yahoo.com")){
            verificacion=existeCorreo(email);
        }
        else if (email.contains("@nix.com")){
            verificacion=existeCorreo(email);
        }
        else {
            etEmail.setError("El correo no tiene una direccion valida");
            verificacion=true;
        }

    return verificacion;
    }

    private boolean existeCorreo(String email) {
        boolean emailEncontrado=conne.validarEmail(email);
        if (emailEncontrado==true){
            etEmail.setError("El correo ya está registrado");
        }
        return emailEncontrado;
    }


    @Override
    public void onCheckedChanged(final CompoundButton compoundButton, boolean b) {
        final boolean[] esProveedor = new boolean[1];
        AlertDialog.Builder aviso = new AlertDialog.Builder(this);
        aviso.setMessage("Ser proveedor significa ser una empresa");
        aviso.setTitle("¿Ser Proveedor?");
        aviso.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Decidiste ser proveedor",
                        Toast.LENGTH_LONG).show();
                compoundButton.setChecked(true);
            }
        });
        aviso.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Decidiste no ser proveedor",
                        Toast.LENGTH_LONG).show();
                compoundButton.setChecked(false);
            }
        });
        AlertDialog avisoCreado = aviso.create();
        if(compoundButton.isChecked())
        {
            avisoCreado.show();
            proovedor=1;
            Toast.makeText(getApplicationContext(),"Variable proovedor= "+proovedor,
                    Toast.LENGTH_LONG).show();
        }
        else{
            proovedor=0;
            Toast.makeText(getApplicationContext(),"Variable proovedor= "+proovedor,
                    Toast.LENGTH_LONG).show();
        }


    }
}
