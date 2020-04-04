package com.example.nixapp.UI.usuario.misEventos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearEvento extends AppCompatActivity implements View.OnClickListener {

    DatePickerDialog picker;
    EditText eTextFecha, eTextHora, nombreEvento, lugarEvento, descripcionEvento, cupoEvento, coverEvento;
    private ArrayList<EventosItems> mEventsList;
    private EventosAdapter mAdapter;
    TimePickerDialog picker2;
    Date currentTime = Calendar.getInstance().getTime();

    Switch simpleSwitch1;
    CheckBox cover;

    NixService nixService;
    NixClient nixClient;
    int privacidad, categoria_evento, dia, ano, mes;
    String clickedName;
    Button terminar, insertar, enables, info;
    int cupo;
    RadioButton r1,r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
        retrofitinit();
        iniciarcomponentes();

        terminar.setOnClickListener(this);
        //////////////////////

        eTextFecha.setInputType(InputType.TYPE_NULL);
        eTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                final Calendar cldr = Calendar.getInstance();
                dia = cldr.get(Calendar.DAY_OF_MONTH);
                mes = cldr.get(Calendar.MONTH);
                ano = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CrearEvento.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eTextFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, ano, mes, dia);
                picker.show();
            }
        });


        eTextHora.setInputType(InputType.TYPE_NULL);
        eTextHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale2 = getResources().getConfiguration().locale;
                Locale.setDefault(locale2);
                final Calendar cldr2 = Calendar.getInstance();
                int hour = cldr2.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr2.get(Calendar.MINUTE);
                // time picker dialog
                picker2 = new TimePickerDialog(CrearEvento.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                eTextHora.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker2.show();
            }
        });

        initList();

        Spinner spinners = findViewById(R.id.spinnerSimple);

        mAdapter = new EventosAdapter(this, mEventsList);
        spinners.setAdapter(mAdapter);

        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventosItems clickedItem = (EventosItems) parent.getItemAtPosition(position);
                categoria_evento= position;
                clickedName = clickedItem.getEventoName();
                Toast.makeText(CrearEvento.this, clickedName + " seleccionada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        simpleSwitch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSwitch1, statusSwitch2;
                if (simpleSwitch1.isChecked()) {
                    info.setVisibility(View.VISIBLE);
                    enables.setVisibility(View.VISIBLE);
                    Toast.makeText(CrearEvento.this, "Ve al catalogo para ver los servicios", Toast.LENGTH_SHORT).show();
                }
                else {
                    info.setVisibility(View.GONE);
                    enables.setVisibility(View.GONE);
                }
            }
        });

        cover = (CheckBox) findViewById(R.id.cover);
        final EditText cover_val = findViewById(R.id.cover_valor);

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSwitch1, statusSwitch2;
                if (cover.isChecked()) {
                    cover_val.setVisibility(View.VISIBLE);
                    Toast.makeText(CrearEvento.this, "Agrega el cover", Toast.LENGTH_SHORT).show();
                }
                else {
                    cover_val.setText("");
                    cover_val.setVisibility(View.GONE);
                }
            }
        });

        final TextView mostrarCorreos = findViewById(R.id.mostrarCorreos);
        final EditText correos = findViewById(R.id.correos);


        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correos.getText().toString().isEmpty()) {
                    Toast.makeText(CrearEvento.this, "Agrega algun correo porfavor", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(validarEmail(correos.getText().toString()))
                    {
                        mostrarCorreos.setText(mostrarCorreos.getText()+"\n" + correos.getText());
                        correos.setText("");
                    }
                    else
                    {
                        Toast.makeText(CrearEvento.this, "Eso no es un correo -.-'", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /////////////////////
    }

    private void iniciarcomponentes() {
        r1=findViewById(R.id.publico);
        r2=findViewById(R.id.privado);
        terminar = findViewById(R.id.eventoTerminado);
        eTextFecha=(EditText) findViewById(R.id.fecha);
        insertar = findViewById(R.id.correoAgregado);
        simpleSwitch1 = (Switch) findViewById(R.id.servicios);
        enables = findViewById(R.id.buttonIrCatalogo);
        info = findViewById(R.id.info);
        eTextHora=(EditText) findViewById(R.id.hora_evento);
        nombreEvento= findViewById(R.id.nomb);
        lugarEvento= findViewById(R.id.direcc);
        cupoEvento= findViewById(R.id.cupo);
        coverEvento=findViewById(R.id.cover_valor);
        descripcionEvento= findViewById(R.id.descripcion);
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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

    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    @Override
    public void onClick(View v) {
        int privacidad= privacidad();
        String nombre = nombreEvento.getText().toString();
        String lugar=lugarEvento.getText().toString();
        String fecha= eTextFecha.getText().toString();
        String hora=(eTextHora.getText().toString())+":00";
        String cupo= cupoEvento.getText().toString();
        String precio= coverEvento.getText().toString();

        String[] fechanueva= fecha.split("/");
                dia=Integer.parseInt(fechanueva[0]);
                mes= Integer.parseInt(fechanueva[1]);
                ano=Integer.parseInt(fechanueva[2]);
                fecha= (ano+"-"+mes+"-"+dia);
        int cover;
        int numCupo=0;
        String descripcion=descripcionEvento.getText().toString();
        if (nombre.isEmpty()){
            nombreEvento.setError("Ingrese Nombre para el evento");
        }
        if (lugar.isEmpty()){
            lugarEvento.setError("Ingrese lugar");
        }
        if (cupo.isEmpty()){
            cupoEvento.setError("Ingrese cupo");
        }
        else{
            numCupo= Integer.parseInt(cupo);
        }
        if (precio.isEmpty()){
            cover=0;
        }else{
            cover=Integer.parseInt(precio);
        }
        if (descripcion.isEmpty()){
            descripcionEvento.setError("Ingrese descripción");
        }
        if (categoria_evento==0){
            Toast.makeText(CrearEvento.this, "Elija una categoria", Toast.LENGTH_SHORT).show();
        }
        else{
            boolean fechacorrecta=verificarFecha(dia, mes, ano);
            if (!fechacorrecta){
                final Eventos requestSample = new Eventos(nombre,privacidad,categoria_evento,fecha,hora,lugar,descripcion,numCupo,cover);
                Call<EventosResult> call= nixService.eventos(requestSample);
                call.enqueue(new Callback<EventosResult>() {
                    @Override
                    public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(CrearEvento.this, "Crear evento correcto", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(CrearEvento.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                            try {
                                Log.i("Error",response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<EventosResult> call, Throwable t) {

                    }
                });

            }
        }

    }

    private boolean verificarFecha(int day, int month, int year) {
        int diff=0;
        if (currentTime.getYear()>year){
            eTextFecha.setError("Esa fecha se encuentra en el pasado");
        }
        else{
            if (currentTime.getMonth() > month ||
                    (currentTime.getMonth() == month && currentTime.getDay() > day)) {
                eTextFecha.setError("Esa fecha se encuentra en el pasado");
            }
            else{
                diff=day-currentTime.getDay();
            }
        }


        if (diff<3){
            eTextFecha.setError("Tienes que tener 3 dias de anticipación");
            return true;
        }
        else{

            return false;
        }
    }

    private int privacidad() {
        if (r1.isChecked()){
            return 0;
        }
        if (r2.isChecked()){
            return 1;
        }
        return 0;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
