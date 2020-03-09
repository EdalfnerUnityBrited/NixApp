package com.example.nixapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Conexion conne= new Conexion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
