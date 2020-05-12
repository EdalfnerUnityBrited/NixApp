package com.example.nixapp.UI.usuario.Calendario;

import android.os.Bundle;
import android.view.View;
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

    class EventosCorregidos{
        Calendar fecha;
        String direccion;
        int cupo;
        int categoria;
        String titulo;
        String estado_asistencia;
        String imagen;
        EventosCorregidos(String titulo,String direccion,Calendar fecha,int categoria,int cupo,String estado_asistencia,String imagen){
            this.fecha = fecha;
            this.categoria = categoria;
            this.titulo = titulo;
            this.direccion = direccion;
            this.cupo = cupo;
            this.estado_asistencia = estado_asistencia;
            this.imagen = imagen;
        }
    }
    CalendarView calendario;
    TextView fecha_presionada;
    Calendar fechaelegida = null;
    List<EventosCorregidos> EventosC = new ArrayList<>();
    List<EventosCorregidos> EventosSeleccionados = new ArrayList<>();
    List<EventDay> events = new ArrayList<>();
    RecyclerView eventospordia;
    CalendarioReciclerview mAdapter;
    NixClient nixClient;
    NixService nixService;
    List<Eventos> eventosUsuario;
    Calendar currentTime = Calendar.getInstance();
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
                    for (Eventos even: eventosUsuario) { //Acomodo los eventos para cambiar la fecha de String a Calendar

                        String date = even.getFecha();
                        String[] dateS = date.split("-");
                        Calendar calendar = Calendar.getInstance();
                        Date fechaEvento = null;
                        try {
                            fechaEvento = new SimpleDateFormat("dd/MM/yyyy").parse(dateS[2]+ "/"+dateS[1]+"/"+dateS[0]);
                            calendar.setTime(fechaEvento);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error al poner fechas",Toast.LENGTH_LONG).show();
                        }
                        if(calendar.get(Calendar.YEAR) >= currentTime.get(Calendar.YEAR))
                        {
                            if(currentTime.get(Calendar.MONTH) < calendar.get(Calendar.MONTH)|| (currentTime.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && currentTime.get(Calendar.DAY_OF_MONTH) <= calendar.get(Calendar.DAY_OF_MONTH)))
                            {
                                EventosCorregidos exem = new EventosCorregidos(even.getNombre_evento(),even.getLugar(),calendar,even.getCategoria_evento(),even.getCupo(),even.getEstado(),even.getFotoPrincipal());
                                EventosC.add(exem);
                            }
                        }


                    }
                    for (EventosCorregidos eventos: EventosC) { //LLeno el calendario con los eventos
                        switch (eventos.categoria)
                        {
                            case 1:
                            {
                                events.add(new EventDay(eventos.fecha, R.drawable.compromisos));
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
                                events.add(new EventDay(eventos.fecha, R.drawable.festejos));
                            }
                            break;
                            case 6:
                            {
                                events.add(new EventDay(eventos.fecha, R.drawable.religiosos));
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
