package com.example.nixapp.UI.proveedor.calendario;

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
import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ContratacionesListResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarioProveedor extends AppCompatActivity {

    class ServicioCorregido{
        Calendar fecha;
        String nombre_servicio;
        String nombre_evento;
        String fecha_servicio;
        String hora_servicio;
        String estado;
        String direccion;

        public ServicioCorregido(Calendar fecha, String nombre_servicio, String nombre_evento, String fecha_servicio, String hora_servicio,String estado,String direccion) {
            this.fecha = fecha;
            this.nombre_servicio = nombre_servicio;
            this.nombre_evento = nombre_evento;
            this.fecha_servicio = fecha_servicio;
            this.hora_servicio = hora_servicio;
            this.estado = estado;
            this.direccion = direccion;
        }



    }
    List<ServicioCorregido> paraCalendario = new ArrayList<>();
    List<ServicioCorregido> paraRecycler = new ArrayList<>();
    List<EventDay> events = new ArrayList<>();
    NixClient nixClient;
    NixService nixService;
    CalendarView calendario;
    TextView fecha_presionada;
    List<Contrataciones> lista;
    Calendar fechaelegida = null;
    Calendar currentTime = Calendar.getInstance();
    RecyclerView serviciospordia;
    CalendarioProveedorReciclerview mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_proveedor);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Calendario");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final Calendar c = Calendar.getInstance();
        calendario = findViewById(R.id.calendario2);
        fecha_presionada = findViewById(R.id.fecha);
        try {
            calendario.setDate(c);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
        retrofitinit();
        Call<ContratacionesListResult> calls = nixService.contrataciones();
        calls.enqueue(new Callback<ContratacionesListResult>() {
            @Override
            public void onResponse(Call<ContratacionesListResult> call, Response<ContratacionesListResult> response) {
                if(response.isSuccessful())
                {
                    lista = response.body().contrataciones;
                    for (Contrataciones cot: lista) { //Acomodo los eventos para cambiar la fecha de String a Calendar

                        String date = cot.getFecha();
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
                                ServicioCorregido corregido = new ServicioCorregido(calendar,cot.getNombre(),cot.getNombre_evento(),cot.getFecha(),cot.getHora(),cot.getEstado_servicio(), cot.getLugar());
                                paraCalendario.add(corregido);
                            }
                        }


                    }
                    for (ServicioCorregido sc: paraCalendario) { //LLeno el calendario con los eventos

                        events.add(new EventDay(sc.fecha, R.drawable.serviciospendientes));

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
                                paraRecycler.clear();
                                for (ServicioCorregido serv: paraCalendario)
                                {
                                    if(serv.fecha.equals(clickedDayCalendar))
                                    {
                                        paraRecycler.add(serv);
                                    }
                                }
                                serviciospordia = findViewById(R.id.serviciospordia);
                                serviciospordia.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                mAdapter = new CalendarioProveedorReciclerview(paraRecycler);
                                serviciospordia.setAdapter(mAdapter);
                            } catch (OutOfDateRangeException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"Error al hacer click",Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                }
                else
                {
                    Toast.makeText(CalendarioProveedor.this,"No se encontraron contrataciones",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ContratacionesListResult> call, Throwable t) {
                Toast.makeText(CalendarioProveedor.this,"Error al obtener contrataciones",Toast.LENGTH_LONG).show();
            }
        });


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
