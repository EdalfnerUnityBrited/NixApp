package com.example.nixapp.UI.usuario.BusquedaEventos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.R;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BusquedaFiltros extends AppCompatActivity {

    EditText fecha1,fecha2;
    int dia, ano, mes,dia2,mes2,ano2;
    DatePickerDialog picker,picker2;
    Chip chip80,chip50,chip100,chip150,chip200,chip250,chip300,chip400p,chip50p,chip100p,chip500p,chip200p,chip800p,chip300p;
    String clickedName;
    private ArrayList<EventosItems> mEventsList;
    private EventosAdapter mAdapter;
    int categoria_evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_cerrar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                BusquedaFiltros.this.overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "Filtros Aplicados", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        fecha1 = findViewById(R.id.fechadesde);
        fecha1.setText(formattedDate);
        fecha1.setInputType(InputType.TYPE_NULL);
        fecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                final Calendar cldr = Calendar.getInstance();
                dia = cldr.get(Calendar.DAY_OF_MONTH);
                mes = cldr.get(Calendar.MONTH);
                ano = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(BusquedaFiltros.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fecha1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, ano, mes, dia);
                picker.show();
            }
        });
        fecha2 = findViewById(R.id.fechahasta);
        fecha2.setText(formattedDate);
        fecha2.setInputType(InputType.TYPE_NULL);
        fecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                final Calendar cldr = Calendar.getInstance();
                dia2 = cldr.get(Calendar.DAY_OF_MONTH);
                mes2 = cldr.get(Calendar.MONTH);
                ano2 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker2 = new DatePickerDialog(BusquedaFiltros.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fecha2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, ano2, mes2, dia2);
                picker2.show();
            }
        });
        inicializarchips();
        String[] Minicipios = new String[]{
                "Elige un municipio:",
                "Acatic",
                "Ameca",
                "Arandas",
                "Atotonilco el alto",
                "Chapala",
                "Cocula",
                "El Arenal",
                "El Salto",
                "Guachinango",
                "Guadalajara",
                "Jocotepec",
                "La Barca",
                "Lagos de Moreno",
                "Mascota",
                "Mazamitla",
                "Mezquitic",
                "Puerto Vallarta",
                "San Juan de los Lagos",
                "Tlaquepaque",
                "Sayula",
                "Tala",
                "Tapalpa",
                "Tequila",
                "Tlajomulco de Zuñiga",
                "Tonala",
                "Tototlan",
                "Zapopan",
                "Zapotlanejo"
        };

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.texto_municipios,Minicipios
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
        spinner.setAdapter(spinnerArrayAdapter);
        initList();
        Spinner spinners = findViewById(R.id.spinnercategoria);

        mAdapter = new EventosAdapter(this, mEventsList);
        spinners.setAdapter(mAdapter);
        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventosItems clickedItem = (EventosItems) parent.getItemAtPosition(position);
                categoria_evento= position;
                clickedName = clickedItem.getEventoName();
                Toast.makeText(BusquedaFiltros.this, clickedName + " seleccionada", Toast.LENGTH_SHORT).show();
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
            BusquedaFiltros.this.overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        }
        return super.onKeyDown(keyCode, event);
    }
    public void inicializarchips()
    {
        chip80 = findViewById(R.id.chip80);
        chip80.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip80.performClick();
            }
        });
        chip80.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip80.isCloseIconVisible()) {
                    chip80.setCloseIconVisible(false);
                } else {
                    chip80.setCloseIconVisible(true);
                    chip50.setCloseIconVisible(false);
                    chip250.setCloseIconVisible(false);
                    chip100.setCloseIconVisible(false);
                    chip150.setCloseIconVisible(false);
                    chip300.setCloseIconVisible(false);
                    chip200.setCloseIconVisible(false);
                }
            }
        });
        chip50 = findViewById(R.id.chip50);
        chip50.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip50.performClick();
            }
        });
        chip50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip50.isCloseIconVisible()) {
                    chip50.setCloseIconVisible(false);
                } else {
                    chip50.setCloseIconVisible(true);
                    chip250.setCloseIconVisible(false);
                    chip80.setCloseIconVisible(false);
                    chip100.setCloseIconVisible(false);
                    chip150.setCloseIconVisible(false);
                    chip300.setCloseIconVisible(false);
                    chip200.setCloseIconVisible(false);
                }
            }
        });
        chip100 = findViewById(R.id.chip100);
        chip100.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip100.performClick();
            }
        });
        chip100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip100.isCloseIconVisible()) {
                    chip100.setCloseIconVisible(false);
                } else {
                    chip100.setCloseIconVisible(true);
                    chip50.setCloseIconVisible(false);
                    chip80.setCloseIconVisible(false);
                    chip250.setCloseIconVisible(false);
                    chip150.setCloseIconVisible(false);
                    chip300.setCloseIconVisible(false);
                    chip200.setCloseIconVisible(false);
                }
            }
        });
        chip150 = findViewById(R.id.chip150);
        chip150.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip150.performClick();
            }
        });
        chip150.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip150.isCloseIconVisible()) {
                    chip150.setCloseIconVisible(false);
                } else {
                    chip150.setCloseIconVisible(true);
                    chip50.setCloseIconVisible(false);
                    chip80.setCloseIconVisible(false);
                    chip100.setCloseIconVisible(false);
                    chip250.setCloseIconVisible(false);
                    chip300.setCloseIconVisible(false);
                    chip200.setCloseIconVisible(false);
                }
            }
        });
        chip200 = findViewById(R.id.chip200);
        chip200.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip200.performClick();
            }
        });
        chip200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip200.isCloseIconVisible()) {
                    chip200.setCloseIconVisible(false);
                } else {
                    chip200.setCloseIconVisible(true);
                    chip50.setCloseIconVisible(false);
                    chip80.setCloseIconVisible(false);
                    chip100.setCloseIconVisible(false);
                    chip150.setCloseIconVisible(false);
                    chip300.setCloseIconVisible(false);
                    chip250.setCloseIconVisible(false);
                }
            }
        });
        chip250 = findViewById(R.id.chip250);
        chip250.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip250.performClick();
            }
        });
        chip250.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip250.isCloseIconVisible()) {
                    chip250.setCloseIconVisible(false);
                } else {
                    chip250.setCloseIconVisible(true);
                    chip50.setCloseIconVisible(false);
                    chip80.setCloseIconVisible(false);
                    chip100.setCloseIconVisible(false);
                    chip150.setCloseIconVisible(false);
                    chip300.setCloseIconVisible(false);
                    chip200.setCloseIconVisible(false);
                }
            }
        });
        chip300 = findViewById(R.id.chip300);
        chip300.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip300.performClick();
            }
        });
        chip300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip300.isCloseIconVisible()) {
                    chip300.setCloseIconVisible(false);
                } else {
                    chip300.setCloseIconVisible(true);
                    chip50.setCloseIconVisible(false);
                    chip80.setCloseIconVisible(false);
                    chip100.setCloseIconVisible(false);
                    chip150.setCloseIconVisible(false);
                    chip250.setCloseIconVisible(false);
                    chip200.setCloseIconVisible(false);
                }
            }
        });
        chip50p = findViewById(R.id.chip50p);
        chip50p.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip50p.performClick();
            }
        });
        chip50p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip50p.isCloseIconVisible()) {
                    chip50p.setCloseIconVisible(false);
                } else {
                    chip50p.setCloseIconVisible(true);
                    chip100p.setCloseIconVisible(false);
                    chip200p.setCloseIconVisible(false);
                    chip300p.setCloseIconVisible(false);
                    chip400p.setCloseIconVisible(false);
                    chip500p.setCloseIconVisible(false);
                    chip800p.setCloseIconVisible(false);
                }
            }
        });
        chip100p = findViewById(R.id.chip100p);
        chip100p.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip100p.performClick();
            }
        });
        chip100p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip100p.isCloseIconVisible()) {
                    chip100p.setCloseIconVisible(false);
                } else {
                    chip100p.setCloseIconVisible(true);
                    chip50p.setCloseIconVisible(false);
                    chip200p.setCloseIconVisible(false);
                    chip300p.setCloseIconVisible(false);
                    chip400p.setCloseIconVisible(false);
                    chip500p.setCloseIconVisible(false);
                    chip800p.setCloseIconVisible(false);
                }
            }
        });
        chip200p = findViewById(R.id.chip200p);
        chip200p.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip200p.performClick();
            }
        });
        chip200p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip200p.isCloseIconVisible()) {
                    chip200p.setCloseIconVisible(false);
                } else {
                    chip200p.setCloseIconVisible(true);
                    chip100p.setCloseIconVisible(false);
                    chip50p.setCloseIconVisible(false);
                    chip300p.setCloseIconVisible(false);
                    chip400p.setCloseIconVisible(false);
                    chip500p.setCloseIconVisible(false);
                    chip800p.setCloseIconVisible(false);
                }
            }
        });
        chip300p = findViewById(R.id.chip300p);
        chip300p.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip300p.performClick();
            }
        });
        chip300p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip300p.isCloseIconVisible()) {
                    chip300p.setCloseIconVisible(false);
                } else {
                    chip300p.setCloseIconVisible(true);
                    chip100p.setCloseIconVisible(false);
                    chip200p.setCloseIconVisible(false);
                    chip50p.setCloseIconVisible(false);
                    chip400p.setCloseIconVisible(false);
                    chip500p.setCloseIconVisible(false);
                    chip800p.setCloseIconVisible(false);
                }
            }
        });
        chip400p = findViewById(R.id.chip400p);
        chip400p.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip400p.performClick();
            }
        });
        chip400p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip400p.isCloseIconVisible()) {
                    chip400p.setCloseIconVisible(false);
                } else {
                    chip400p.setCloseIconVisible(true);
                    chip100p.setCloseIconVisible(false);
                    chip200p.setCloseIconVisible(false);
                    chip300p.setCloseIconVisible(false);
                    chip50p.setCloseIconVisible(false);
                    chip500p.setCloseIconVisible(false);
                    chip800p.setCloseIconVisible(false);
                }
            }
        });
        chip500p = findViewById(R.id.chip500p);
        chip500p.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip500p.performClick();
            }
        });
        chip500p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip500p.isCloseIconVisible()) {
                    chip500p.setCloseIconVisible(false);
                } else {
                    chip500p.setCloseIconVisible(true);
                    chip100p.setCloseIconVisible(false);
                    chip200p.setCloseIconVisible(false);
                    chip300p.setCloseIconVisible(false);
                    chip400p.setCloseIconVisible(false);
                    chip50p.setCloseIconVisible(false);
                    chip800p.setCloseIconVisible(false);
                }
            }
        });
        chip800p = findViewById(R.id.chip800p);
        chip800p.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip800p.performClick();
            }
        });
        chip800p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip800p.isCloseIconVisible()) {
                    chip800p.setCloseIconVisible(false);
                } else {
                    chip800p.setCloseIconVisible(true);
                    chip100p.setCloseIconVisible(false);
                    chip200p.setCloseIconVisible(false);
                    chip300p.setCloseIconVisible(false);
                    chip400p.setCloseIconVisible(false);
                    chip500p.setCloseIconVisible(false);
                    chip50p.setCloseIconVisible(false);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtros, menu);


        return super.onCreateOptionsMenu(menu);
    }
    private void initList() {
        mEventsList = new ArrayList<>();
        mEventsList.add(new EventosItems("Elige una categoria", R.drawable.select_some));
        mEventsList.add(new EventosItems("Compromisos", R.drawable.compromisos));
        mEventsList.add(new EventosItems("Mega Eventos", R.drawable.mega));
        mEventsList.add(new EventosItems("Galas", R.drawable.galas));
        mEventsList.add(new EventosItems("Empresariales", R.drawable.empresariales));
        mEventsList.add(new EventosItems("Festejos", R.drawable.festejos));
        mEventsList.add(new EventosItems("Religiosos", R.drawable.religiosos));
    }

}
