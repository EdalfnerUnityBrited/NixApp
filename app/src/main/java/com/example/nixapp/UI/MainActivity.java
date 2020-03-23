package com.example.nixapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Conexion;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Conexion conne = new Conexion();

    Button btnLogin, btnSignup, btnSample;
    EditText etSample;
    NixClient nixClient;
    NixService nixService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnSignup = (Button) findViewById(R.id.buttonSignup);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);


        retrofitInit();
        conne.connectionBD();
        conexionExitosa();
    }

    private void retrofitInit() {
        nixClient = NixClient.getInstance();
        nixService = nixClient.getNixService();
    }

    private void conexionExitosa() {
        if (conne.conexion != null) {
            Toast.makeText(getApplicationContext(), "La conexion se ha establecido", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Conexion erronea", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        Intent q;
        switch (i) {
            case R.id.buttonLogin: {
                q = new Intent(view.getContext(), InicioSesion.class);
                startActivity(q);
                break;
            }
            case R.id.buttonSignup: {
                q = new Intent(view.getContext(), CrearCuenta.class);
                startActivity(q);
                break;
            }
        }
    }


}
