package com.example.nixapp.UI.usuario.Tendencias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.InfoEventoExpandida;
import com.example.nixapp.UI.usuario.eventosProximos.EventosProximos;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tendencias extends AppCompatActivity implements EventosTendenciasFragment.OnListFragmentInteractionListener {

    NixService nixService;
    NixClient nixClient;
    Eventos eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitInit();
        setContentView(R.layout.activity_tendencias);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Tendencias");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onListFragmentInteraction(Eventos item) {


        Busqueda busqueda= new Busqueda(item.getId());
        Call<EventosResult> call= nixService.getEventId(busqueda);
        call.enqueue(new Callback<EventosResult>() {
            @Override
            public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                if (response.isSuccessful()){
                    eventos= response.body().eventos;
                    Intent intentInfoExpandida = new Intent(Tendencias.this, InfoEventoExpandida.class);
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
    public void setToolbarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
