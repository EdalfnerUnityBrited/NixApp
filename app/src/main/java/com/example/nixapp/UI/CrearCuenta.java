package com.example.nixapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nixapp.R;
public class CrearCuenta extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    Switch switchProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        switchProveedor = (Switch) findViewById(R.id.switchProveedor);

        switchProveedor.setTextOn("Si");
        switchProveedor.setTextOff("No");
        switchProveedor.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {

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
        avisoCreado.show();
    }
}
