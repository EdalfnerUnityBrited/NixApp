package com.example.nixapp.UI.welcome;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.MenuPrincipalUsuarioGeneral;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.LoginResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioSesion extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView tvGoSignUp;
    EditText etEmail, etPassword;
    Intent intent;
    NixService nixService;
    NixClient nixClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        btnLogin=findViewById(R.id.login);
        etEmail=findViewById(R.id.username);
        etPassword=findViewById(R.id.password);

        btnLogin.setOnClickListener(this);

        retrofitInit();

    }

    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    @Override
    public void onClick(View v) {
        String email = etEmail.getText().toString();
        String password= etPassword.getText().toString();
        if (email.isEmpty()&&password.isEmpty()){
            etEmail.setError("Ingrese Email");
            etPassword.setError("Ingrese Contraseña");
        }
        if (email.isEmpty()){
            etEmail.setError("Ingrese Email");
        }
        if (password.isEmpty()){
            etPassword.setError("Ingrese Password");
        }
        else{
            goToLogin();
        }

    }

    private void goToLogin() {
        String email= etEmail.getText().toString();
        String password= etPassword.getText().toString();
        final Usuario requestSample = new Usuario(email, password);
        Call<LoginResult> call = nixService.login(requestSample);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InicioSesion.this, "Sesion Iniciada", Toast.LENGTH_SHORT).show();
                    response.body().procesarRespuesta();
                    Intent i= new Intent(InicioSesion.this, MenuPrincipalUsuarioGeneral.class);
                    i.putExtra("usuario", response.body().usuario);

                    Log.i("Sesion Iniciada",response.body().toString());
                    startActivity(i);
                    finish();
                } else {
                    try {
                        Log.i("Error",response.errorBody().string().toString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Toast.makeText(InicioSesion.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
