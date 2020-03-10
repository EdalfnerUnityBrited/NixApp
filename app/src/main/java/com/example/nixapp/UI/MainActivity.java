package com.example.nixapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nixapp.DB.Conexion;
import com.example.nixapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Conexion conne= new Conexion();
    
    Button btnLogin,btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnSignup = (Button) findViewById(R.id.buttonSignup);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

        conne.connectionBD();
        conexionExitosa();
    }

    private void conexionExitosa() {
        if (conne.conexion!=null){
            Toast.makeText(getApplicationContext(),"La conexion se ha establecido",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Conexion erronea",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        Intent q;
        switch (i){
            case R.id.buttonLogin:{
                q = new Intent(view.getContext(),InicioSesion.class);
                startActivity(q);
                break;
            }
            case R.id.buttonSignup:{
                break;
            }
        }
    }
}
