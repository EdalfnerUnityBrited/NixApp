package com.example.nixapp.UI.proveedor.misServicios;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.R;

public class CrearArticulosDatos extends AppCompatActivity {

    Spinner categorias,precio_por;
    String[] categorias_texto = new String[]{"Mobiliario","Sonido","Terrazas/Casinos/Terrenos", "Alimentos y Bebidas" , "Licores" , "Botanas", "Decoracion" , "Personal/Empleados", "Desechables", "Cristaleria"};
    String[] preciopor = new String[]{ "Unidad" ,"Hora","Persona"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_articulo);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Intent intent = new Intent(getApplicationContext(), *.class);
                //startActivity(intent);
                //CrearEvento.this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        categorias = findViewById(R.id.spinner_categorias);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.texto_municipios,categorias_texto
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
        categorias.setAdapter(spinnerArrayAdapter);
        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        precio_por = findViewById(R.id.spinner_precioPor);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                this,R.layout.texto_municipios,preciopor
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
        precio_por.setAdapter(spinnerArrayAdapter1);
        precio_por.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
