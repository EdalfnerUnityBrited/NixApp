package com.example.nixapp.UI.usuario.eventosProximos;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Notificaciones;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.InfoEventoExpandida;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosProximos extends AppCompatActivity implements EventosProximosFragment.OnListFragmentInteractionListener, MisNotificacionesFragment.OnListFragmentInteractionListener{

    NixService nixService;
    NixClient nixClient;
    Eventos eventos;
    int fragment_origen = 1;
    int fragment_destino = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_proximos);
        retrofitInit();
        BottomNavigationView bottomNav = findViewById(R.id.menu_abajo_eventos_proximos);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Eventos Próximos");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_eventos_proximos,new EventosProximosFragment()).commit();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void setToolbarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_eventosproximos_menu:{
                            setToolbarTitle("Eventos Próximos");
                            selectedFragment = new EventosProximosFragment();
                            fragment_destino = 1;
                            break;
                        }
                        case R.id.nav_misnotificaciones:{
                            setToolbarTitle("Mis Notificaciones");
                            selectedFragment = new MisNotificacionesFragment();
                            fragment_destino = 3;
                            break;
                        }
                        case R.id.nav_misintereses:{
                            setToolbarTitle("Mis Intereses");
                            selectedFragment = new MisInteresesFragment();
                            fragment_destino = 2;
                            break;
                        }
                    }
                    if(fragment_destino == 1 && (fragment_origen == 2||fragment_origen == 3))
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.fragment_container_eventos_proximos, selectedFragment).commit();
                        fragment_origen = 1;
                    }
                    else if(fragment_destino == 2 && fragment_origen == 1)
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).replace(R.id.fragment_container_eventos_proximos, selectedFragment).commit();
                        fragment_origen = 2;
                    }
                    else if(fragment_destino == 2 && fragment_origen == 3)
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.fragment_container_eventos_proximos, selectedFragment).commit();
                        fragment_origen = 2;
                    }
                    else if(fragment_destino == 3 && (fragment_origen == 2||fragment_origen == 1))
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).replace(R.id.fragment_container_eventos_proximos, selectedFragment).commit();
                        fragment_origen = 3;
                    }
                    return true;
                }
            };

    @Override
    public void onListFragmentInteraction(Eventos item) {


        Busqueda busqueda= new Busqueda(item.getId());
        Call<EventosResult> call= nixService.getEventId(busqueda);
        call.enqueue(new Callback<EventosResult>() {
            @Override
            public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                if (response.isSuccessful()){
                    eventos= response.body().eventos;
                    Intent intentInfoExpandida = new Intent(EventosProximos.this, InfoEventoExpandida.class);
                    intentInfoExpandida.putExtra("nombre",eventos.getNombre_evento());
                    intentInfoExpandida.putExtra("privacidad",eventos.getPrivacidad());
                    intentInfoExpandida.putExtra("categoria",eventos.getCategoria_evento());
                    intentInfoExpandida.putExtra("fecha",eventos.getFecha());
                    intentInfoExpandida.putExtra("hora",eventos.getHora());
                    intentInfoExpandida.putExtra("lugar",eventos.getLugar());
                    intentInfoExpandida.putExtra("descripcion",eventos.getDescripcion());
                    intentInfoExpandida.putExtra("cupo",eventos.getCupo());
                    intentInfoExpandida.putExtra("cover",eventos.getCover());
                    intentInfoExpandida.putExtra("fotoPrincipal",eventos.getFotoPrincipal());
                    intentInfoExpandida.putExtra("id",eventos.getId());
                    intentInfoExpandida.putExtra("municipio",eventos.getMunicipio());
                    startActivity(intentInfoExpandida);
                }
                else{

                }
            }

            @Override
            public void onFailure(Call<EventosResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void onNotificationFragmentInteraction(Notificaciones item) {
        Notificaciones notificaciones= item;
        if (notificaciones.getTipoNotificacion()==3||notificaciones.getTipoNotificacion()==4){

        }
        else{

            Busqueda busqueda= new Busqueda(Integer.toString(item.getId_evento()));
            Call<EventosResult> call= nixService.getEventId(busqueda);
            call.enqueue(new Callback<EventosResult>() {
                @Override
                public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                    if (response.isSuccessful()){
                        eventos= response.body().eventos;
                        Intent intentInfoExpandida = new Intent(EventosProximos.this, InfoEventoExpandida.class);
                        intentInfoExpandida.putExtra("nombre",eventos.getNombre_evento());
                        intentInfoExpandida.putExtra("privacidad",eventos.getPrivacidad());
                        intentInfoExpandida.putExtra("categoria",eventos.getCategoria_evento());
                        intentInfoExpandida.putExtra("fecha",eventos.getFecha());
                        intentInfoExpandida.putExtra("hora",eventos.getHora());
                        intentInfoExpandida.putExtra("lugar",eventos.getLugar());
                        intentInfoExpandida.putExtra("descripcion",eventos.getDescripcion());
                        intentInfoExpandida.putExtra("cupo",eventos.getCupo());
                        intentInfoExpandida.putExtra("cover",eventos.getCover());
                        intentInfoExpandida.putExtra("fotoPrincipal",eventos.getFotoPrincipal());
                        intentInfoExpandida.putExtra("id",eventos.getId());
                        intentInfoExpandida.putExtra("municipio",eventos.getMunicipio());
                        startActivity(intentInfoExpandida);
                    }
                    else{

                    }
                }

                @Override
                public void onFailure(Call<EventosResult> call, Throwable t) {

                }
            });

        }
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
