package com.example.nixapp.UI.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.DB.controllers.TokenController;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.MenuPrincipalUsuarioGeneral;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.modelotablas.UsuarioToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        UsuarioToken token = TokenController.getToken();

        if (token == null)
            pantallaInicioSesion();
        else
            principal(token);
    }


    void principal(UsuarioToken usuarioToken) {


        NixClient nixClient = NixClient.getInstance();
        NixService nixService = nixClient.getNixService();
        Call<Usuario> call = nixService.getUser();
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(WelcomeActivity.this,
                            MenuPrincipalUsuarioGeneral.class);
                    intent.putExtra("usuario", response.body());
                    startActivity(intent);
                } else if (response.code() == 401) {
                    pantallaInicioSesion();
                } else {
                    Toast.makeText(WelcomeActivity.this,
                            "Revisa tu conexion a internet.\n Intentalo de nuevo mas tarde",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(WelcomeActivity.this,
                        "Revisa tu conexion a internet.\n Intentalo de nuevo mas tarde",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    void pantallaInicioSesion() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

}
