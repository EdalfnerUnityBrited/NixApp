package com.example.nixapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nixapp.DB.Conexion;
import com.example.nixapp.R;
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
            if (email.contains("@hotmail.com")){
                verificarCorreo(email);
            }
            else if(email.contains("@gmail.com")){
                verificarCorreo(email);
            }
            else if(email.contains("@outlook.com")){
                verificarCorreo(email);
            }
            else if (email.contains("@yahoo.com")){
                verificarCorreo(email);
            }
            else if (email.contains("@nix.com")){
                Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
                verificarCorreo(email);
            }
            else {
                etEmail.setError("El correo no tiene una direccion valida");
            }
        }

    }

    private void verificarCorreo(String email) {
        boolean emailEncontrado=conne.validarEmail(email);
        if (emailEncontrado==true){
            Toast.makeText(getApplicationContext(),"Bienvenido",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),"Vete",Toast.LENGTH_LONG).show();
        }
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
