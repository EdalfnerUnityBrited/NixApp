package com.example.nixapp.UI.welcome;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearCuenta extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    Switch switchProveedor;
    int proovedor=0;
    Button btnCrear;
    EditText etNombre, etApellidoP, etApellidoM, etEmail, etTelefono, etFechaNac, etPassword, etPasswordConf;
    Intent intent;
    private int ano, mes, dia;
    Date currentTime = Calendar.getInstance().getTime();
    String date;
    NixClient nixClient;
    NixService nixService;

    private static final String TAG = "MainActivity";

    private TextView mDisplayDate=null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

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
        etPasswordConf=findViewById(R.id.passwordConfirmacion);
        btnCrear=findViewById(R.id.buttonCrearCuenta);
        etPassword=findViewById(R.id.password);

        switchProveedor.setOnCheckedChangeListener(this);
        btnCrear.setOnClickListener(this);
        //////////////
        mDisplayDate = (TextView)findViewById(R.id.fecha_nacimiento);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CrearCuenta.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "-" + day + "-" + year);
                ano=year;
                mes=month;
                dia=day;
                date = year + "-" + month + "-" + day;
                mDisplayDate.setText(date);
            }
        };
        /////////////
        retrofitInit();
    }

    private void retrofitInit() {
        nixClient = NixClient.getInstance();
        nixService = nixClient.getNixService();
    }

    @Override
    public void onClick(View view) {
        String email = etEmail.getText().toString();
        String password= etPassword.getText().toString();
        String nombre = etNombre.getText().toString();
        String apellidoPaterno= etApellidoP.getText().toString();
        String apellidoMaterno = etApellidoM.getText().toString();
        String telefono= etTelefono.getText().toString();
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
        if (mDisplayDate==null){
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
                    boolean verificarMayor= verificarCumpleaños(ano, mes, dia);
                    if (!verificarMayor){
                                final Usuario requestSample = new Usuario(proovedor,nombre,apellidoPaterno,apellidoMaterno, email, date,password, telefono,5,"",passwordConfirmacion);


                                Call<ResponseBody> call = nixService.usuario(requestSample);

                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(CrearCuenta.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                                            Intent i= new Intent(CrearCuenta.this, InicioSesion.class);
                                            try {
                                                Log.i("Cuenta Creada",response.body().string().toString());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            startActivity(i);
                                            finish();

                                        } else {
                                            try {
                                                Log.i("Error",response.errorBody().string().toString());
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            }
                                            Toast.makeText(CrearCuenta.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                            try {

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                    }
                    }
                }
            }
            }
            }

        }



    private boolean verificarCumpleaños(int year, int month, int day) {

        int diff = (currentTime.getYear()+1900) - year;
        Log.d(TAG, "Año cumpleaños"+year);
        Log.d(TAG, "Año actual"+(currentTime.getYear()+1900));
        if (currentTime.getMonth() > month ||
                (currentTime.getMonth() == month && currentTime.getDay() >= day)) {
            diff--;
        }
        Log.d(TAG, "Años " + diff);
        if (diff<13){
            mDisplayDate.setError("Tienes que ser mayor a 13 años");
            return true;
        }
        else{
            return false;
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



    private boolean verificarCorreo(String email) {
        boolean verificacion = false;
        if (email.contains("@hotmail.com")){
        }
        else if(email.contains("@gmail.com")){
        }
        else if(email.contains("@outlook.com")){

        }
        else if (email.contains("@yahoo.com")){
        }
        else if (email.contains("@nix.com")){
        }
        else {
            etEmail.setError("El correo no tiene una direccion valida");
            verificacion=true;
        }

    return verificacion;
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

        }
        else{
            proovedor=0;

        }


    }
}
