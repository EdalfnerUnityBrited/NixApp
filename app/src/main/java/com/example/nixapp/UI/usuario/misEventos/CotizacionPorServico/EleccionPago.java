package com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.R;

public class EleccionPago extends AppCompatActivity {

    Button efectivo, linea;
    int tipo_bono;
    String total_pago;
    double cargo_total;
    AlertDialog.Builder resumen_compra;
    String tipo = "",id_cotizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_pago);
        efectivo= findViewById(R.id.botonEfectivo);
        linea= findViewById(R.id.botonLinea);
        tipo_bono=(int) getIntent().getSerializableExtra("Pago");
        total_pago=(String) getIntent().getSerializableExtra("total del pago");
        id_cotizacion = (String) getIntent().getSerializableExtra("id_cotizacion");
        if(tipo_bono == 1)
        {
            cargo_total = (Float.valueOf(total_pago) * 0.1);
            tipo = "el Deposito";
        }
        else
        {
            String[] separado1 = total_pago.split("\\$");
            String[] separado2 = separado1[1].split("MXN");
            cargo_total = (Float.valueOf(separado2[0])*0.9);
            tipo = "la Liquidacion";

        }

        linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumen_compra = new AlertDialog.Builder(EleccionPago.this);
                resumen_compra.setTitle("Importe total:");
                resumen_compra.setMessage("Se cobrara el importe de: $" + cargo_total + "  MXN \nSimbolizando " + tipo + " del Servicio");
                resumen_compra.setCancelable(false);
                resumen_compra.setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Intent intent= new Intent(EleccionPago.this, PagoLinea.class);
                        intent.putExtra("Cargo", cargo_total);
                        intent.putExtra("tipo_bono", tipo);
                        intent.putExtra("id_cotizacion", id_cotizacion);
                        startActivity(intent);
                        EleccionPago.this.finish();
                    }
                });
                resumen_compra.setNegativeButton("No, es incorrecto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                resumen_compra.show();

            }
        });
        efectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumen_compra = new AlertDialog.Builder(EleccionPago.this);
                resumen_compra.setTitle("Importe total:");
                resumen_compra.setMessage("Se cobrara el importe de: $" + cargo_total + "  MXN \nSimbolizando " + tipo + " del Servicio");
                resumen_compra.setCancelable(false);
                resumen_compra.setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(EleccionPago.this, "Pago Realizado", Toast.LENGTH_SHORT).show();
                    }
                });
                resumen_compra.setNegativeButton("No, es incorrecto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                resumen_compra.show();
            }
        });
    }
}
