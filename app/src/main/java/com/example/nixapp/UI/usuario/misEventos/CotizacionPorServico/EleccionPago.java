package com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nixapp.R;

public class EleccionPago extends AppCompatActivity {

    Button efectivo, linea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_pago);
        efectivo= findViewById(R.id.botonEfectivo);
        linea= findViewById(R.id.botonLinea);
        linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(EleccionPago.this, PagoLinea.class);
                startActivity(intent);
            }
        });
        efectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
