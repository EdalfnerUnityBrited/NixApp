package com.example.nixapp.UI.welcome;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarContra extends AppCompatActivity {

    EditText fecha;
    Button confirmar;
    AlertDialog.Builder dialogo1;
    NixService nixService;
    NixClient nixClient;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);
        retrofitInit();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        fecha= findViewById(R.id.fecha);
        confirmar= findViewById(R.id.confirmar);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date= fecha.getText().toString();
                Toast.makeText(RecuperarContra.this, date, Toast.LENGTH_SHORT).show();
                if (date.equals(usuario.fechaNac)){
                    dialogo1 = new AlertDialog.Builder(RecuperarContra.this);
                    dialogo1.setTitle("Cambio de Contraseña");
                    dialogo1.setMessage("Ingrese tu nueva contraseña del correo: " + usuario.email);
                    LinearLayout layout = new LinearLayout(RecuperarContra.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
                    final EditText titleBox = new EditText(RecuperarContra.this);
                    titleBox.setHint("Contraseña");
                    titleBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    titleBox.setTypeface(Typeface.DEFAULT);
                    layout.addView(titleBox); // Notice this is an add method

// Add another TextView here for the "Description" label
                    final EditText descriptionBox = new EditText(RecuperarContra.this);
                    descriptionBox.setHint("Confirmar Contraseña");
                    descriptionBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    descriptionBox.setTypeface(Typeface.DEFAULT);
                    layout.addView(descriptionBox); // Another add method

                    dialogo1.setView(layout);
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar cambios", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            String contrasena= titleBox.getText().toString();
                            String confirmacion= descriptionBox.getText().toString();
                            Usuario forgot= new Usuario(confirmacion, Integer.toString(usuario.id), contrasena);
                            Call<ResponseBody> call= nixService.passwordForgot(forgot);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(RecuperarContra.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(RecuperarContra.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(RecuperarContra.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //cancelar();
                        }
                    });
                    dialogo1.show();
                }
                else{
                    Toast.makeText(RecuperarContra.this, "Error en la fecha", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
