package com.example.nixapp.UI.usuario.misEventos.BusquedaEventos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.InfoEventoExpandida;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosListResult;
import com.example.nixapp.conn.results.EventosResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscarServicios extends AppCompatActivity{

    String municipio, fechaIni, fechaFin, cupo, cover;
    int categoria;
    TextView textView;
    Button filtros;
    private NixService nixService;
    private NixClient nixClient;
    public List<Eventos> eventosList;
    private RecyclerView recyclerView;
    Eventos eventos;
    ServiciosRecyclerViewAdapter adapterEventos;
    private BuscarServicios.OnListFragmentInteractionListener mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_eventos);
        recyclerView=findViewById(R.id.recyclerEvents);
        retrofitinit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backarrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intentBusqueda = new Intent(getApplicationContext(), BusquedaFiltrosServicio.class);
                finish();
                startActivity(intentBusqueda);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                return false;
            }
        });
        try {
            municipio=(String) getIntent().getSerializableExtra("municipio");
            cupo=(String) getIntent().getSerializableExtra("cupo");
            cover=(String) getIntent().getSerializableExtra("cover");
            fechaIni=(String) getIntent().getSerializableExtra("fechaInicio");
            fechaFin=(String) getIntent().getSerializableExtra("fechaFinal");
            categoria=(int) getIntent().getSerializableExtra("categoria");
        }
        catch (Exception e){
            cupo="";
            cover="";
            fechaIni="";
            fechaFin="";
            municipio="";
            categoria=0;
        }
        Toast.makeText(this, cupo, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_busqueda, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getText(R.string.search));
        //searchView.setOutlineAmbientShadowColor(Color.WHITE);
        //searchView.setOutlineSpotShadowColor();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String nombre=query;
                Busqueda buscar= new Busqueda(municipio, fechaIni, fechaFin, cupo, cover, nombre, categoria);
                Call<EventosListResult> call = nixService.buscarEvento(buscar);
                call.enqueue(new Callback<EventosListResult>() {
                    @Override
                    public void onResponse(Call<EventosListResult> call, Response<EventosListResult> response) {
                        if (response.isSuccessful()){
                            eventosList=response.body().eventos;

                            adapterEventos = new ServiciosRecyclerViewAdapter(eventosList, new OnListFragmentInteractionListener() {
                                @Override
                                public void onListFragmentInteraction(Eventos item) {

                                    Busqueda busqueda= new Busqueda(item.getId());
                                    Call<EventosResult> call= nixService.getEventId(busqueda);
                                    call.enqueue(new Callback<EventosResult>() {
                                        @Override
                                        public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                                            if (response.isSuccessful()){
                                                eventos= response.body().eventos;
                                                Intent intentInfoExpandida = new Intent(BuscarServicios.this, InfoEventoExpandida.class);
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
                                public void onClick(View v) {

                                }
                            });
                            recyclerView.setAdapter(adapterEventos);
                            recyclerView.setLayoutManager(new LinearLayoutManager(BuscarServicios.this));
                            Toast.makeText(BuscarServicios.this, "Eventos Buscados", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Log.i("error",response.errorBody().toString());
                            Toast.makeText(BuscarServicios.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventosListResult> call, Throwable t) {
                        Toast.makeText(BuscarServicios.this, "Error en la llamada", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
        //check http://stackoverflow.com/questions/11085308/changing-the-background-drawable-of-the-searchview-widget
        //View searchPlate = (View) searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        //searchPlate.setBackgroundResource(R.mipmap.textfield_custom);

        return super.onCreateOptionsMenu(menu);
    }


    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
    public List<Eventos> getDataFragment(){
        return eventosList;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Eventos item);
        void onClick(View v);
        
    }

 

}
