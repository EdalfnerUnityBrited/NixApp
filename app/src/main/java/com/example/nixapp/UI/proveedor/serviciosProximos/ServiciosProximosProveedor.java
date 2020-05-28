package com.example.nixapp.UI.proveedor.serviciosProximos;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.Chat;
import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Notificaciones;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.InfoExpandidaServicio;
import com.example.nixapp.UI.usuario.InfoEventoExpandida;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.ChatActivity;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiciosProximosProveedor extends AppCompatActivity implements ChatsFragmentProveedor.OnListFragmentInteractionListener, NotificacionesFragmentProveedor.OnListFragmentInteractionListener, ServiciosPendientesFragmentProveedor.OnListFragmentInteractionListener{
    Usuario usuario;
    Eventos eventos;
    NixService nixService;
    NixClient nixClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitInit();
        setContentView(R.layout.activity_servicios_proximos_proveedor);
        Toolbar mToolbar = findViewById(R.id.toolbarServicios);
        mToolbar.setTitle("Servicios Proximos");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_abajo_servicios_proximos_proveedor);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_servicios_proximos_proveedor,new ServiciosPendientesFragmentProveedor()).commit();
    }
    public void setToolbarTitle(String title) {
        Toolbar toolbar = findViewById(R.id.toolbarServicios);
        toolbar.setTitle(title);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_serviciospendientes:{
                            setToolbarTitle("Servicios Proximos");
                            selectedFragment = new ServiciosPendientesFragmentProveedor();
                            break;
                        }
                        case R.id.nav_notificaciones_proveedor:{
                            setToolbarTitle("Mis Notificaciones");
                            selectedFragment = new NotificacionesFragmentProveedor();
                            break;
                        }
                        case R.id.nav_chats_proveedor:{
                            setToolbarTitle("Mis Chats");
                            selectedFragment = new ChatsFragmentProveedor();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_servicios_proximos_proveedor,selectedFragment).commit();
                    return true;
                }
            };
    @Override
    public void onListFragmentInteraction(Chat item) {
        String idProveedor= String.valueOf(item.getId());

        Intent chatSelected= new Intent(getBaseContext(), ChatActivity.class);
        chatSelected.putExtra("sender", usuario.id);
        chatSelected.putExtra("reciver", idProveedor);
        startActivity(chatSelected);

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
                        Intent intentInfoExpandida = new Intent(ServiciosProximosProveedor.this, InfoEventoExpandida.class);
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

    @Override
    public void onListFragmentInteraction(Contrataciones item) {
        Intent intent= new Intent(ServiciosProximosProveedor.this, InfoExpandidaServicio.class);
        intent.putExtra("id_contratacion", item.getId());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
