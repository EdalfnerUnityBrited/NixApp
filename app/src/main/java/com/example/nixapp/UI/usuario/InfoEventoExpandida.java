package com.example.nixapp.UI.usuario;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.EventosAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ImagenResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoEventoExpandida extends AppCompatActivity {

    private String nombre = "", fecha, hora, lugar, descripcion = "", fotoPrincipal;
    private int privacidad, categoria, cupo, cover;
    EditText nombreEvento,direccion_evento,fecha_evento,hora_evento,cupo_event,descripcion_evento,cover_evento;
    CheckBox cover_even;
    RadioButton publico,privado;
    ImageView principal;
    Spinner spinners;
    private EventosAdapter mAdapter;
    private ArrayList<EventosItems> mEventsList;
    NixClient nixClient;
    NixService nixService;
    List<ImagenEventos> eventosUsuario;

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
        privacidad = Integer.parseInt((String) getIntent().getSerializableExtra("privacidad"));
        categoria = Integer.parseInt((String) getIntent().getSerializableExtra("categoria"));
        cupo = Integer.parseInt((String) getIntent().getSerializableExtra("cupo"));
        cover = Integer.parseInt((String) getIntent().getSerializableExtra("cover"));
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
                    if(eventosUsuario.isEmpty())
                    {
                        //Toast.makeText(getApplicationContext(),"Esta vacio...",Toast.LENGTH_LONG).show();
                        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(),eventosUsuario);
                        viewPager.setAdapter(viewPagerAdapter);
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),String.valueOf(eventosUsuario.size()),Toast.LENGTH_LONG).show();
                        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(),eventosUsuario);
                        viewPager.setAdapter(viewPagerAdapter);
                    }
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
        ///////////////////////////////////////////////////
        if(cover == 0)
        {
            cover_even.setChecked(false);
            cover_even.setEnabled(false);
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
}
