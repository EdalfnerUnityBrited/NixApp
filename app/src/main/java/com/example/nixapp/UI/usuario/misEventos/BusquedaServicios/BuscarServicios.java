package com.example.nixapp.UI.usuario.misEventos.BusquedaServicios;

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

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.BusquedaArticulos;
import com.example.nixapp.DB.BusquedaPaquetes;
import com.example.nixapp.DB.BusquedaServicios;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.misServicios.ServiciosRecyclerViewAdapter;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ArticulosListResult;
import com.example.nixapp.conn.results.PaquetesListResult;
import com.example.nixapp.conn.results.ServiciosListResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscarServicios extends AppCompatActivity{

    String municipio, fechaIni, fechaFin, cupo, cover, nombre, precioIni, precioFin, precioPor;
    int categoria, catArticulo;
    TextView textView;
    Button filtros;
    private NixService nixService;
    private NixClient nixClient;
    public List<Eventos> eventosList;
    private RecyclerView recyclerView;
    Eventos eventos;
    int tipoBusqueda;
    ServiciosBuscarRecyclerViewAdapter adapterEventos;
    PaqueteBuscarRecyclerViewAdapter adapterPaquete;
    ArticuloBuscarRecyclerViewAdapter adapterArticulo;
    List<CatalogoServicios> serviciosList;
    List<Paquetes> paquetesList;
    List<Articulos> articulosList;
    private BuscarServicios.OnListFragmentInteractionListener mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paquetesList= new ArrayList<>();
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
            tipoBusqueda=(int) getIntent().getSerializableExtra("tipo");
        }
        catch (Exception e){
            tipoBusqueda=0;
        }
        try {
            categoria=(int) getIntent().getSerializableExtra("categoria");
        }
        catch (Exception e){
            categoria=5;
        }

        Toast.makeText(this, cupo, Toast.LENGTH_SHORT).show();
        BusquedaServicios busquedaServicios= new BusquedaServicios("",categoria);
        Call<ServiciosListResult> call = nixService.buscarServicio(busquedaServicios);
        call.enqueue(new Callback<ServiciosListResult>() {
            @Override
            public void onResponse(Call<ServiciosListResult> call, Response<ServiciosListResult> response) {
                if (response.isSuccessful()){
                    serviciosList=  response.body().servicios;
                    adapterEventos=  new ServiciosBuscarRecyclerViewAdapter(serviciosList, new OnListFragmentInteractionListener() {
                        @Override
                        public void onListFragmentInteraction(CatalogoServicios item) {
                            Toast.makeText(BuscarServicios.this, "Me apretaste", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onClick(View v) {

                        }

                        @Override
                        public void onPaqueteFragmentInteraction(Paquetes mItem) {

                        }

                        @Override
                        public void onListArticuloInteraction(Articulos mItem) {

                        }
                    });
                    recyclerView.setAdapter(adapterEventos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(BuscarServicios.this));
                }
                else{
                    Toast.makeText(BuscarServicios.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                    Log.i("error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ServiciosListResult> call, Throwable t) {

            }
        });

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

                if (tipoBusqueda==0){
                    try {
                        categoria=(int) getIntent().getSerializableExtra("categoria");
                    }
                    catch (Exception e){
                        categoria=5;
                    }
                    BusquedaServicios buscarServicios= new BusquedaServicios(nombre,categoria);
                    Call<ServiciosListResult> call = nixService.buscarServicio(buscarServicios);
                    call.enqueue(new Callback<ServiciosListResult>() {
                        @Override
                        public void onResponse(Call<ServiciosListResult> call, Response<ServiciosListResult> response) {

                            if (response.isSuccessful()){
                                serviciosList=  response.body().servicios;
                                adapterEventos=  new ServiciosBuscarRecyclerViewAdapter(serviciosList, new OnListFragmentInteractionListener() {
                                    @Override
                                    public void onListFragmentInteraction(CatalogoServicios item) {
                                        Toast.makeText(BuscarServicios.this, "Me apretaste", Toast.LENGTH_SHORT).show();
                                    }


                                    @Override
                                    public void onClick(View v) {

                                    }

                                    @Override
                                    public void onPaqueteFragmentInteraction(Paquetes mItem) {

                                    }

                                    @Override
                                    public void onListArticuloInteraction(Articulos mItem) {

                                    }
                                });
                                recyclerView.setAdapter(adapterEventos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(BuscarServicios.this));
                            }
                            else{
                                Toast.makeText(BuscarServicios.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                Log.i("error", response.errorBody().toString());
                            }

                        }

                        @Override
                        public void onFailure(Call<ServiciosListResult> call, Throwable t) {

                        }
                    });
                }
                else if (tipoBusqueda==1){
                    try {

                        precioIni=(String) getIntent().getSerializableExtra("precioIni");
                        precioFin=(String) getIntent().getSerializableExtra("precioFin");
                    }
                    catch (Exception e){
                        categoria=5;
                    }
                    BusquedaPaquetes busquedaPaquetes= new BusquedaPaquetes(nombre,precioIni,precioFin);
                    Call<PaquetesListResult> call = nixService.buscarPaquete(busquedaPaquetes);
                    call.enqueue(new Callback<PaquetesListResult>() {
                        @Override
                        public void onResponse(Call<PaquetesListResult> call, Response<PaquetesListResult> response) {


                            if (response.isSuccessful()){
                                paquetesList=  response.body().paquetes;
                                adapterPaquete=  new PaqueteBuscarRecyclerViewAdapter(paquetesList, new OnListFragmentInteractionListener() {
                                    @Override
                                    public void onListFragmentInteraction(CatalogoServicios item) {

                                    }

                                    @Override
                                    public void onClick(View v) {

                                    }

                                    @Override
                                    public void onPaqueteFragmentInteraction(Paquetes mItem) {
                                        Toast.makeText(BuscarServicios.this, "Me apretaste", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onListArticuloInteraction(Articulos mItem) {

                                    }
                                });
                                        recyclerView.setAdapter(adapterEventos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(BuscarServicios.this));
                            }
                            else{
                                Toast.makeText(BuscarServicios.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                Log.i("error", response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<PaquetesListResult> call, Throwable t) {

                        }
                    });
                }
                else if (tipoBusqueda==2){
                    try {
                        precioIni=(String) getIntent().getSerializableExtra("precioIni");
                        precioFin=(String) getIntent().getSerializableExtra("precioFin");
                        precioPor=(String) getIntent().getSerializableExtra("precioPor");
                        catArticulo=(int) getIntent().getSerializableExtra("categoriaArticulo");
                    }
                    catch (Exception e){
                        categoria=5;
                    }
                    BusquedaArticulos busquedaArticulos= new BusquedaArticulos(nombre,precioPor,precioIni,precioFin,catArticulo);
                    Call<ArticulosListResult> call= nixService.buscarArticulos(busquedaArticulos);
                    call.enqueue(new Callback<ArticulosListResult>() {
                        @Override
                        public void onResponse(Call<ArticulosListResult> call, Response<ArticulosListResult> response) {



                            if (response.isSuccessful()){
                                articulosList=  response.body().articulos;
                                adapterArticulo=  new ArticuloBuscarRecyclerViewAdapter(articulosList, new OnListFragmentInteractionListener() {
                                    @Override
                                    public void onListFragmentInteraction(CatalogoServicios item) {

                                    }

                                    @Override
                                    public void onClick(View v) {

                                    }

                                    @Override
                                    public void onPaqueteFragmentInteraction(Paquetes mItem) {

                                    }

                                    @Override
                                    public void onListArticuloInteraction(Articulos mItem) {
                                        Toast.makeText(BuscarServicios.this, "Articulo apretado", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                recyclerView.setAdapter(adapterEventos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(BuscarServicios.this));
                            }
                            else{
                                Toast.makeText(BuscarServicios.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                Log.i("error", response.errorBody().toString());
                            }

                        }

                        @Override
                        public void onFailure(Call<ArticulosListResult> call, Throwable t) {

                        }
                    });
                }
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
        void onListFragmentInteraction(CatalogoServicios item);
        void onClick(View v);
        void onPaqueteFragmentInteraction(Paquetes mItem);
        void onListArticuloInteraction(Articulos mItem);
    }
    


 

}
