package com.example.nixapp.UI.usuario;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.DB.Prospectos;
import com.example.nixapp.DB.RespuestaEventoLleno;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.EventosAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventoLlenoResult;
import com.example.nixapp.conn.results.ImagenResult;
import com.example.nixapp.conn.results.ProspectosResult;
import com.example.nixapp.conn.results.UsuarioListResult;
import com.example.nixapp.conn.results.UsuarioResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoEventoExpandida extends AppCompatActivity {

    private String nombre = "", fecha, hora, lugar, descripcion = "", fotoPrincipal, id, municipio;
    private int privacidad, categoria, cupo, cover;
    EditText nombreEvento,direccion_evento,fecha_evento,hora_evento,cupo_event,descripcion_evento,cover_evento, municipios, nombre_Anfitrion, email_Anfitrion, telefono_Anfitrion;
    CheckBox cover_even;
    TextView invitadosConfirmados;
    RadioButton publico,privado;
    ImageView principal;
    Spinner spinners;
    Button asistire,interes,imagenes,confirmarAsistencia;
    private EventosAdapter mAdapter;
    private ArrayList<EventosItems> mEventsList;
    NixClient nixClient;
    NixService nixService;
    boolean Meinteresa = false, Voyir = false;
    List<ImagenEventos> eventosUsuario;
    LinearLayout botones;
    String confir = "";
    LinearLayout linearCover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_evento_expandida);



        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        nombre = (String) getIntent().getSerializableExtra("nombre");
        fecha = (String) getIntent().getSerializableExtra("fecha");
        hora = (String) getIntent().getSerializableExtra("hora");
        lugar = (String) getIntent().getSerializableExtra("lugar");
        descripcion = (String) getIntent().getSerializableExtra("descripcion");
        fotoPrincipal = (String) getIntent().getSerializableExtra("fotoPrincipal");
        privacidad = (int) getIntent().getSerializableExtra("privacidad");
        categoria = (int) getIntent().getSerializableExtra("categoria");
        cupo = (int) getIntent().getSerializableExtra("cupo");
        cover = (int) getIntent().getSerializableExtra("cover");
        id =(String) getIntent().getSerializableExtra("id");
        municipio=(String) getIntent().getSerializableExtra("municipio");

        setToolbarTitle(nombre);
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        String[] fechaSeparada = fecha.split("-");
        String[] horaSeparada = hora.split(":");
        retrofitinit();
        final Eventos event =new Eventos(nombre);
        Call<ImagenResult> call = nixService.buscarImagenes(event);
        call.enqueue(new Callback<ImagenResult>() {
            @Override
            public void onResponse(Call<ImagenResult> call, Response<ImagenResult> response) {
                if(response.isSuccessful())
                {

                    eventosUsuario = response.body().imagenEventos;
                    final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
                    if(eventosUsuario.isEmpty())
                    {
                        //Toast.makeText(getApplicationContext(),"Esta vacio...",Toast.LENGTH_LONG).show();

                        eventosUsuario.add(new ImagenEventos("https://pbs.twimg.com/media/DqiOx30WkAEcWEg.jpg","1"));
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),String.valueOf(eventosUsuario.size()),Toast.LENGTH_LONG).show();

                    }
                    final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(),eventosUsuario);
                    viewPager.setAdapter(viewPagerAdapter);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),response.errorBody().toString(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ImagenResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error en el intento",Toast.LENGTH_LONG).show();
            }
        });
        ///////////////////////////////////////////////////
        publico = findViewById(R.id.publico);
        privado = findViewById(R.id.privado);
        spinners = findViewById(R.id.spinnerSimple);
        nombreEvento = findViewById(R.id.nomb);
        direccion_evento = findViewById(R.id.direcc);
        fecha_evento = findViewById(R.id.fecha);
        hora_evento = findViewById(R.id.hora_evento);
        cupo_event = findViewById(R.id.cupo);
        descripcion_evento = findViewById(R.id.descripcion);
        cover_even = findViewById(R.id.cover);
        cover_evento = findViewById(R.id.cover_valor);
        interes = findViewById(R.id.Meinteresa);
        asistire = findViewById(R.id.asistire);
        municipios = findViewById(R.id.muni);
        botones = findViewById(R.id.lineabotones);
        email_Anfitrion = findViewById(R.id.email_Anf);
        nombre_Anfitrion = findViewById(R.id.nomb_Anf);
        telefono_Anfitrion = findViewById(R.id.tels_Anf);
        invitadosConfirmados = findViewById(R.id.invitados_confirmados);
        confirmarAsistencia = findViewById(R.id.confirmarAsistencia);
        ///////////////////////////////////////////////////
        if (verificarFechayHora()){
            interes.setEnabled(false);
            asistire.setEnabled(false);
            interes.setVisibility(View.INVISIBLE);
            asistire.setVisibility(View.INVISIBLE);
            confirmarAsistencia.setEnabled(true);
            confirmarAsistencia.setVisibility(View.VISIBLE);
        }
        ///////////////////////
        Eventos nuevo = new Eventos(id);
        Call<UsuarioListResult> confirmados = nixService.confirmados(nuevo);
        confirmados.enqueue(new Callback<UsuarioListResult>() {
            @Override
            public void onResponse(Call<UsuarioListResult> call, Response<UsuarioListResult> response) {
                if(response.isSuccessful())
                {
                    List<Usuario> confirmados = response.body().usuarios;
                    for (Usuario asistente: confirmados)
                    {
                        confir += asistente.name + " " + asistente.apellidoP + " " + asistente.apellidoM + "\n";
                    }
                    if(confirmados.isEmpty())
                    {
                        invitadosConfirmados.setText("Puedes ser el primero en confirmar tu asistencia!");
                    }
                    else
                    {
                        invitadosConfirmados.setText(confir);
                    }

                }
                else
                {
                    Toast.makeText(InfoEventoExpandida.this, "Error al encontrar asistentes:" + response.message() , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioListResult> call, Throwable t) {
                Toast.makeText(InfoEventoExpandida.this, "Error Anfitrion: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ///////////////////////////////////////////////////

        Call<UsuarioResult> InfoAnfitrion = nixService.infoAnfitrion(nuevo);
        InfoAnfitrion.enqueue(new Callback<UsuarioResult>() {
            @Override
            public void onResponse(Call<UsuarioResult> call, Response<UsuarioResult> response) {
                if(response.isSuccessful())
                {
                    Usuario anfitrion = response.body().usuario;
                    nombre_Anfitrion.setText(anfitrion.name + " " + anfitrion.apellidoP + " "+ anfitrion.apellidoM);
                    nombre_Anfitrion.setBackground(null);
                    nombre_Anfitrion.setEnabled(false);
                    email_Anfitrion.setText(anfitrion.email);
                    email_Anfitrion.setBackground(null);
                    email_Anfitrion.setEnabled(false);
                    telefono_Anfitrion.setText(anfitrion.telefono);
                    telefono_Anfitrion.setBackground(null);
                    telefono_Anfitrion.setEnabled(false);
                }
                else
                {
                    Toast.makeText(InfoEventoExpandida.this, "Error al conseguir info anfitrion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioResult> call, Throwable t) {
                Toast.makeText(InfoEventoExpandida.this, "Error Anfitrion: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //////////////////////////////////////////////////
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(privacidad == 0)
        {
            privado.setChecked(false);
            publico.setChecked(true);
            privado.setClickable(false);
            publico.setClickable(false);
        }
        else
        {
            privado.setChecked(true);
            publico.setChecked(false);
            privado.setClickable(false);
            publico.setClickable(false);
        }

        /////////////////////////////////////////////////
        initList();
        mAdapter = new EventosAdapter(this, mEventsList);
        spinners.setAdapter(mAdapter);
        spinners.setSelection(categoria);
        spinners.setBackground(null);
        spinners.setEnabled(false);
        //////////////////////////////////////////////////
        nombreEvento.setText(nombre);
        nombreEvento.setBackground(null);
        nombreEvento.setEnabled(false);
        direccion_evento.setText(lugar);
        direccion_evento.setBackground(null);
        direccion_evento.setEnabled(false);
        fecha_evento.setText(fechaSeparada[2]+"/"+fechaSeparada[1]+"/"+fechaSeparada[0]);
        fecha_evento.setBackground(null);
        fecha_evento.setEnabled(false);
        hora_evento.setText(horaSeparada[0]+":"+horaSeparada[1]);
        hora_evento.setBackground(null);
        hora_evento.setEnabled(false);
        cupo_event.setText(String.valueOf(cupo));
        cupo_event.setBackground(null);
        cupo_event.setEnabled(false);
        descripcion_evento.setText(descripcion);
        descripcion_evento.setBackground(null);
        descripcion_evento.setEnabled(false);
        municipios.setText(municipio);
        municipios.setBackground(null);
        municipios.setEnabled(false);
        linearCover = findViewById(R.id.linearCover);
        ///////////////////////////////////////////////////
        if(cover == 0)
        {
            linearCover.setVisibility(View.GONE);
            cover_even.setVisibility(View.GONE);
        }
        else
        {
            cover_even.setChecked(true);
            cover_even.setEnabled(false);
            cover_evento.setVisibility(View.VISIBLE);
            cover_evento.setText("$"+ String.valueOf(cover));
            cover_evento.setBackground(null);
            cover_evento.setEnabled(false);
        }
        Call<EventoLlenoResult> eventoLleno = nixService.eventoLleno(new Eventos(id));
        eventoLleno.enqueue(new Callback<EventoLlenoResult>() {
            @Override
            public void onResponse(Call<EventoLlenoResult> call, Response<EventoLlenoResult> response) {
                if (response.isSuccessful()){
                    RespuestaEventoLleno eventolleno1 = response.body().eventoLleno;
                    if (eventolleno1.id_evento.equals("0")){
                        interes.setEnabled(false);
                        asistire.setEnabled(false);
                        interes.setText("Evento lleno");
                        asistire.setText("Evento lleno");
                    }
                    else if (eventolleno1.id_evento.equals("1")){

                    }
                }
            }

            @Override
            public void onFailure(Call<EventoLlenoResult> call, Throwable t) {
                Toast.makeText(InfoEventoExpandida.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Call<ProspectosResult> Cprospecto = nixService.Confirmacionprospecto(new Prospectos(id));
        Cprospecto.enqueue(new Callback<ProspectosResult>() {
            @Override
            public void onResponse(Call<ProspectosResult> call, Response<ProspectosResult> response) {
                if (response.isSuccessful()) {
                    Prospectos prospecto1 = response.body().prospectos;
                    if(prospecto1 == null)
                    {
                        Toast.makeText(InfoEventoExpandida.this,"No eres participe aun....", Toast.LENGTH_LONG).show();
                        asistire.setBackgroundResource(R.drawable.custombutom2);
                        asistire.setTextColor(R.drawable.butom_text);
                        asistire.setText("Confirmar asistencia");
                        interes.setBackgroundResource(R.drawable.custombutom2);
                        interes.setTextColor(R.drawable.butom_text);
                        interes.setText("Me interesa");

                    }
                    else
                    {
                        if(prospecto1.getEstado().equals("confirmado"))
                        {
                            asistire.setBackgroundResource(R.drawable.custombutom);
                            asistire.setTextColor(R.drawable.buttom_text2);
                            asistire.setText("Cancelar asistencia");
                            interes.setBackgroundResource(R.drawable.custombutom2);
                            interes.setTextColor(R.drawable.butom_text);
                            interes.setText("Me interesa");
                            Voyir = true;
                        }
                        else if(prospecto1.getEstado().equals("me interesa"))
                        {
                            interes.setBackgroundResource(R.drawable.button_yellow);
                            interes.setTextColor(R.drawable.buttom_text2);
                            interes.setText("Ya no me interesa");
                            Meinteresa = true;
                            asistire.setBackgroundResource(R.drawable.custombutom2);
                            asistire.setTextColor(R.drawable.butom_text);
                            asistire.setText("Confirmar asistencia");
                        }
                        else
                        {
                            interes.setVisibility(View.GONE);
                            asistire.setVisibility(View.GONE);
                            botones.setVisibility(View.GONE);
                        }

                    }

                }
                else
                {
                    Toast.makeText(InfoEventoExpandida.this, "Fallo en Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProspectosResult> call, Throwable t) {
                Toast.makeText(InfoEventoExpandida.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        asistire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Voyir == false)
                {
                    Call<ResponseBody> prospecto = nixService.prospecto(new Prospectos(id,"confirmado"));
                    prospecto.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(InfoEventoExpandida.this, response.message(), Toast.LENGTH_SHORT).show();
                                asistire.setBackgroundResource(R.drawable.custombutom);
                                asistire.setTextColor(R.drawable.buttom_text2);
                                asistire.setText("Cancelar asistencia");
                                Voyir = true;
                                interes.setBackgroundResource(R.drawable.custombutom2);
                                interes.setTextColor(R.drawable.butom_text);
                                interes.setText("Me interesa");
                                Meinteresa = false;
                            }
                            else
                            {
                                Toast.makeText(InfoEventoExpandida.this, "Fallo al confirmar su asistencia", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(InfoEventoExpandida.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Call<ResponseBody> prospecto = nixService.prospecto(new Prospectos(id,"cancelar"));
                    prospecto.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(InfoEventoExpandida.this, response.message(), Toast.LENGTH_SHORT).show();
                                asistire.setBackgroundResource(R.drawable.custombutom2);
                                asistire.setTextColor(R.drawable.butom_text);
                                asistire.setText("Confirmar asistencia");
                                Voyir = false;
                            }
                            else
                            {
                                Toast.makeText(InfoEventoExpandida.this, "Fallo al cancelar su asitencia", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(InfoEventoExpandida.this, "Error2: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        interes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Meinteresa == false)
                {
                    Call<ResponseBody> prospecto = nixService.prospecto(new Prospectos(id,"me interesa"));
                    prospecto.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(InfoEventoExpandida.this, response.message(), Toast.LENGTH_SHORT).show();
                                interes.setBackgroundResource(R.drawable.button_yellow);
                                interes.setTextColor(R.drawable.buttom_text2);
                                interes.setText("Ya no me interesa");
                                Meinteresa = true;
                                asistire.setBackgroundResource(R.drawable.custombutom2);
                                asistire.setTextColor(R.drawable.butom_text);
                                asistire.setText("Confirmar asistencia");
                                Voyir = false;
                            }
                            else
                            {
                                Toast.makeText(InfoEventoExpandida.this, "Fallo al poner interes", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(InfoEventoExpandida.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Call<ResponseBody> prospecto = nixService.prospecto(new Prospectos(id,"cancelar"));
                    prospecto.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(InfoEventoExpandida.this, response.message(), Toast.LENGTH_SHORT).show();
                                interes.setBackgroundResource(R.drawable.custombutom2);
                                interes.setTextColor(R.drawable.butom_text);
                                interes.setText("Me interesa");
                                Meinteresa = false;
                            }
                            else
                            {
                                Toast.makeText(InfoEventoExpandida.this, "Fallo al quitar interes", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(InfoEventoExpandida.this, "Error2: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        confirmarAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[][] direccionFinal = {new String[1]};
                final String[][] distanciaFinal = {new String[1]};
                String lat = String.valueOf(MenuPrincipalUsuarioGeneral.getLatitudUsuario());
                String lng = String.valueOf(MenuPrincipalUsuarioGeneral.getLongitudUsuario());
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?&latlng=%1$s+,+%2$s&key=AIzaSyAPGGYxsJfpi3DY0o11lAR4-Gccfpf3juw",lat,lng);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Toast.makeText(InfoEventoExpandida.this, "ERROR API GEOCODING", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();
                            InfoEventoExpandida.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String[] direccion1 = myResponse.split("\"formatted_address\"+ +:+ +\"");
                                    direccionFinal[0] = direccion1[1].split("\"");
                                    String url2 = String.format("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=%1$s+Jalisco+Mexico&destinations=%2$s+Jalisco+Mexico&key=AIzaSyAPGGYxsJfpi3DY0o11lAR4-Gccfpf3juw", direccionFinal[0][0],lugar);
                                    segundaLlamada(url2);
                                }
                            });
                        }
                    }
                });


            }
        });
    }

    private boolean verificarFechayHora(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(currentTime);
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = dt.format(currentTime);
        if (fecha.equals(formattedDate) && hora.compareTo(formattedTime)<0){
            return true;
        }
        else{
            return false;
        }
    }

    public void setToolbarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
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
    private void segundaLlamada(String url){
        final String[][] distanciaFinal = {new String[1]};
        OkHttpClient cliente2 = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        cliente2.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    final String myResponse2 = response.body().string();
                    InfoEventoExpandida.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] distancia1 = myResponse2.split("\"text\"+ :+ \"");
                            distanciaFinal[0] = distancia1[1].split("\"");
                            if (distanciaFinal[0][0].compareTo("900 ft")<0){
                                Toast.makeText(InfoEventoExpandida.this, "Estas ahi!", Toast.LENGTH_SHORT).show();
                                Prospectos prospectos= new Prospectos(id);
                                Call<ResponseBody> callAsist= nixService.confirmarAsistencia(prospectos);
                                callAsist.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(InfoEventoExpandida.this, "Asistencia confirmada", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(InfoEventoExpandida.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            else {
                                Toast.makeText(InfoEventoExpandida.this, "Estas muy lejos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}
