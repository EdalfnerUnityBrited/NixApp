package com.example.nixapp.UI.usuario.misEventos.BusquedaServicios;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.R;

import java.util.ArrayList;

public class BusquedaFiltrosServicio extends AppCompatActivity {

    int dia, ano, mes,dia2,mes2,ano2;
    DatePickerDialog picker,picker2;

    String clickedName;
    private ArrayList<ServiciosItems> mEventsList;
    private ServiciosAdapter mAdapter;
    CheckBox filt_s,filt_a,filt_p;
    int categoria_evento, categoria_articulo;
    String precioMin, precioMax;
    Spinner spinner;
    EditText precioMinArt, precioMaxArt, precioMinPaq,precioMaxpaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros_servicios);
        precioMinArt= findViewById(R.id.precio_minArt);
        precioMaxArt=findViewById(R.id.precio_maxArt);
        precioMinPaq=findViewById(R.id.precio_minPaq);
        precioMaxpaq=findViewById(R.id.precio_maxPaq);


        precioMin="";
        precioMax="";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_cerrar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                BusquedaFiltrosServicio.this.overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Toast.makeText(getApplicationContext(), "Filtros Aplicados", Toast.LENGTH_SHORT).show();

                if (filt_s.isChecked()){

                    Intent busqueda = new Intent(getApplicationContext(), BuscarServicios.class);
                    busqueda.putExtra("tipo",0);
                    busqueda.putExtra("categoria", categoria_evento);
                    startActivity(busqueda);
                    finish();
                }
                else if (filt_a.isChecked()){
                    String preciomin=precioMinArt.getText().toString();
                    String preciomax=precioMaxArt.getText().toString();
                    if (preciomin.isEmpty()){
                        preciomin="";
                    }
                    if (preciomax.isEmpty()){
                        preciomax="";
                    }

                    Intent busqueda = new Intent(getApplicationContext(), BuscarServicios.class);
                    busqueda.putExtra("tipo",2);
                    busqueda.putExtra("categoriaArticulo",categoria_articulo);
                    busqueda.putExtra("precioFin",preciomax);
                    busqueda.putExtra("precioIni",preciomin);
                    startActivity(busqueda);
                    finish();
                }
                else if (filt_p.isChecked()){
                    String preciomin=precioMinPaq.getText().toString();
                    String preciomax=precioMaxpaq.getText().toString();
                    if (preciomin.isEmpty()){
                        preciomin="";
                    }
                    if (preciomax.isEmpty()){
                        preciomax="";
                    }

                    Intent busqueda = new Intent(getApplicationContext(), BuscarServicios.class);
                    busqueda.putExtra("tipo",1);
                    busqueda.putExtra("precioFin",preciomax);
                    busqueda.putExtra("precioIni",preciomin);
                    startActivity(busqueda);
                    finish();
                }

                return false;
            }
        });
        filt_a = findViewById(R.id.filtro_art);
        filt_p = findViewById(R.id.filtro_paq);
        filt_s = findViewById(R.id.filtro_ser);
        filt_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filt_a.isChecked())
                {
                    filt_p.setChecked(false);
                    filt_s.setChecked(false);
                }
            }
        });
        filt_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filt_p.isChecked())
                {
                    filt_a.setChecked(false);
                    filt_s.setChecked(false);
                }
            }
        });
        filt_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filt_s.isChecked())
                {
                    filt_p.setChecked(false);
                    filt_a.setChecked(false);
                }
            }
        });
        String[] categorias_texto = new String[]{"Mobiliario","Sonido","Terrazas/Casinos/Terrenos", "Alimentos y Bebidas" , "Licores" , "Botanas", "Decoracion" , "Personal/Empleados", "Desechables", "Cristaleria"};


        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.texto_municipios,categorias_texto
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria_articulo= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        initList();
        Spinner spinners = findViewById(R.id.spinnercategoria);

        mAdapter = new ServiciosAdapter(this, mEventsList);
        spinners.setAdapter(mAdapter);
        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ServiciosItems clickedItem = (ServiciosItems) parent.getItemAtPosition(position);
                categoria_evento= position;
                clickedName = clickedItem.getEventoName();

                Toast.makeText(BusquedaFiltrosServicio.this, clickedName + " seleccionada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            finish();
            BusquedaFiltrosServicio.this.overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtros, menu);


        return super.onCreateOptionsMenu(menu);
    }
    private void initList() {
        mEventsList = new ArrayList<>();
        mEventsList.add(new ServiciosItems("Elige una categoria", R.drawable.select_some));
        mEventsList.add(new ServiciosItems("Compromisos", R.drawable.compromisos));
        mEventsList.add(new ServiciosItems("Mega Eventos", R.drawable.mega));
        mEventsList.add(new ServiciosItems("Galas", R.drawable.galas));
        mEventsList.add(new ServiciosItems("Empresariales", R.drawable.empresariales));
        mEventsList.add(new ServiciosItems("Festejos", R.drawable.festejos));
        mEventsList.add(new ServiciosItems("Religiosos", R.drawable.religiosos));
    }

}
