package com.example.nixapp.UI.usuario.Calendario;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosListResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Calendario extends AppCompatActivity {

    class Evento{
        String fecha;
        String direccion;
        String titulo;
        int cupo;
        int categoria;
        String estado_asistencia;
        Evento(String titulo,String direccion,String fecha,int categoria,int cupo,String estado_asistencia){
            this.fecha = fecha;
            this.categoria = categoria;
            this.titulo = titulo;
            this.direccion = direccion;
            this.cupo = cupo;
            this.estado_asistencia = estado_asistencia;
        }
    }
    class EventosCorregidos{
        Calendar fecha;
        String direccion;
        int cupo;
        int categoria;
        String titulo;
        String estado_asistencia;
        EventosCorregidos(String titulo,String direccion,Calendar fecha,int categoria,int cupo,String estado_asistencia){
            this.fecha = fecha;
            this.categoria = categoria;
            this.titulo = titulo;
            this.direccion = direccion;
            this.cupo = cupo;
            this.estado_asistencia = estado_asistencia;
        }
    }
    boolean call = true;
    CalendarView calendario;
    TextView fecha_presionada;
    Calendar fechaelegida = null;
    List<Evento> TodosEventos = new ArrayList<>();
    List<EventosCorregidos> EventosC = new ArrayList<>();
    List<EventosCorregidos> EventosSeleccionados = new ArrayList<>();
    List<EventDay> events = new ArrayList<>();
    RecyclerView eventospordia;
    CalendarioReciclerview mAdapter;
    NixClient nixClient;
    NixService nixService;
    List<Eventos> eventosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        retrofitinit();
        Call<EventosListResult> calls = nixService.eventosAsistenciaUsuario();
        calls.enqueue(new Callback<EventosListResult>() {
            @Override
            public void onResponse(Call<EventosListResult> call, Response<EventosListResult> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Objetos Obtenidos Correctamente",Toast.LENGTH_LONG).show();
                    eventosUsuario = response.body().eventos;

                }
                else
                {
                    Toast.makeText(getApplicationContext(),response.errorBody().toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EventosListResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Calendario");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //////////////////
        final Calendar c = Calendar.getInstance();
        calendario = findViewById(R.id.calendario2);
        fecha_presionada = findViewById(R.id.fecha);
        try {
            calendario.setDate(c);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
        Evento ev1 = new Evento("Cumpleaños de Jerry", "Calle Palermo #11","2020/05/26",1,50 ,"Te interesa");
        Evento ev2 = new Evento("Pedononon por Cuarentena", "Calle Suiza #12","2020/05/26",2,500,"Te interesa");
        Evento ev3 = new Evento("Boda de Norma", "Cielito #1414","2020/06/05",3,120,"Asistire");
        Evento ev4 = new Evento("Nostredemus Lo supo", "Calle Futuro #11","2020/05/22",5,50,"Asistire");
        Evento ev5 = new Evento("Carnita Asada", "Calle Palermo #11","2020/05/28",6,150,"Asistire");
        Evento ev6 = new Evento("Cumpleaños Damaris", "Calle Palermo #112","2020/07/01",1,510,"Asistire");
        Evento ev7 = new Evento("Cumpleaños de Jerry", "Calle Palermo #11","2020/05/26",4,150,"Lo creaste");
        Evento ev8 = new Evento("Toda la empresa ALV", "Calle ChupaElPerro #111","2020/04/30",4,5110,"Lo creaste");
        Evento ev9 = new Evento("No se que poner", "Calle Palermo #11","2020/05/01",1,50,"Lo creaste");
        Evento ev10 = new Evento("Si funciona", "Calle Palermo #11","2020/05/13",1,520,"Lo creaste");
        TodosEventos.add(ev1);
        TodosEventos.add(ev2);
        TodosEventos.add(ev3);
        TodosEventos.add(ev4);
        TodosEventos.add(ev5);
        TodosEventos.add(ev6);
        TodosEventos.add(ev7);
        TodosEventos.add(ev8);
        TodosEventos.add(ev9);
        TodosEventos.add(ev10);//Simula los eventos almacenados con su info agregada


        if(call == true)//Simular si la query sale bien
        {
            for (Evento even: TodosEventos) { //Acomodo los eventos para cambiar la fecha de String a Calendar

                String date = even.fecha;
                String[] dateS = date.split("/");
                Calendar calendar = Calendar.getInstance();
                Date fechaEvento = null;
                try {
                    fechaEvento = new SimpleDateFormat("dd/MM/yyyy").parse(dateS[2]+ "/"+dateS[1]+"/"+dateS[0]);
                    calendar.setTime(fechaEvento);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error al poner fechas",Toast.LENGTH_LONG).show();
                }
                EventosCorregidos exem = new EventosCorregidos(even.titulo,even.direccion,calendar,even.categoria,even.cupo,even.estado_asistencia);
                EventosC.add(exem);

            }
            for (EventosCorregidos eventos: EventosC) { //LLeno el calendario con los eventos
                switch (eventos.categoria)
                {
                    case 1:
                    {
                        events.add(new EventDay(eventos.fecha, R.drawable.festejos));
                    }
                    break;
                    case 2:
                    {
                        events.add(new EventDay(eventos.fecha, R.drawable.mega));
                    }
                    break;
                    case 3:
                    {
                        events.add(new EventDay(eventos.fecha, R.drawable.galas));
                    }
                    break;
                    case 4:
                    {
                        events.add(new EventDay(eventos.fecha, R.drawable.empresariales));
                    }
                    break;
                    case 5:
                    {
                        events.add(new EventDay(eventos.fecha, R.drawable.religiosos));
                    }
                    break;
                    case 6:
                    {
                        events.add(new EventDay(eventos.fecha, R.drawable.compromisos));
                    }
                    break;

                }
            }
            calendario.setEvents(events);
            calendario.setOnDayClickListener(new OnDayClickListener() {
                @Override
                public void onDayClick(EventDay eventDay) {
                    Calendar clickedDayCalendar = eventDay.getCalendar();
                    int year = clickedDayCalendar.get(Calendar.YEAR);
                    int mes = clickedDayCalendar.get(Calendar.MONTH);
                    int dia = clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
                    fechaelegida = clickedDayCalendar;
                    try {
                        calendario.setDate(clickedDayCalendar);
                        fecha_presionada.setText("Fecha seleccionada: "+dia +"/"+(mes+1)+"/"+year);
                        EventosSeleccionados.clear();
                        for (EventosCorregidos eventos: EventosC)
                        {
                            if(eventos.fecha.equals(clickedDayCalendar))
                            {
                                EventosSeleccionados.add(eventos);
                            }
                        }
                        eventospordia = findViewById(R.id.eventosperdia);
                        eventospordia.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mAdapter = new CalendarioReciclerview(EventosSeleccionados);
                        eventospordia.setAdapter(mAdapter);
                    } catch (OutOfDateRangeException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Error al hacer click",Toast.LENGTH_LONG).show();
                    }


                }
            });
        }
        else
        {

        }
    }
    public void setToolbarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }
    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
