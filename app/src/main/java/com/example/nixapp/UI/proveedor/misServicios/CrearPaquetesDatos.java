package com.example.nixapp.UI.proveedor.misServicios;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.R;

public class CrearPaquetesDatos extends AppCompatActivity {

    TableLayout stk;
    Button articulos_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paquetes_datos);
        init();
        articulos_add = findViewById(R.id.articulo_necesario);
        articulos_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tbrow = new TableRow(getApplicationContext());
                tbrow.setId(View.generateViewId());
                TextView t1v = new TextView(getApplicationContext());
                t1v.setText("5");
                t1v.setTextColor(Color.WHITE);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(getApplicationContext());
                t2v.setText("Por Cantidad");
                t2v.setTextColor(Color.WHITE);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(getApplicationContext());
                t3v.setText("Chorizo");
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                stk.addView(tbrow);
            }
        });

    }
    public void init() {
        stk = (TableLayout) findViewById(R.id.table_main);
        stk.setGravity(Gravity.CENTER);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("  Cantidad  ");
        tv0.setTextSize(20);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("  Precio por  ");
        tv1.setTextSize(20);
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("  Articulo  ");
        tv2.setTextSize(20);
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);


    }
}
