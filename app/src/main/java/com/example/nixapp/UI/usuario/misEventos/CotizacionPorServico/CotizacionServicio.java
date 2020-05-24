package com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.misServicios.Articulos.ArticuloRecyclerViewAdapter;
import com.example.nixapp.UI.proveedor.misServicios.CrearServicioMenu;
import com.example.nixapp.UI.usuario.misEventos.BusquedaServicios.ArticuloServicioRecyclerViewAdapter;
import com.example.nixapp.UI.usuario.misEventos.BusquedaServicios.PaqueteServicioRecyclerViewAdapter;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ArticulosListResult;
import com.example.nixapp.conn.results.PaquetesListResult;
import com.example.nixapp.conn.results.ServicioResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CotizacionServicio extends AppCompatActivity {
    int servicioid;
    CheckBox lunes, martes, miercoles, jueves, viernes, sabado, domingo;
    NixService nixService;
    NixClient nixClient;
    TextView hora_inicio,hora_fin,direccion, name, telefono, nombreProveedor;
    TextView municipios;
    List<Articulos> articulosList;
    List<Paquetes> paquetesList;
    ArticuloServicioRecyclerViewAdapter articuloAdapter;
    PaqueteServicioRecyclerViewAdapter paqueteAdapter;
    RecyclerView recyclerArticles, recyclerPaquetes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articulosList= new ArrayList<>();
        paquetesList= new ArrayList<>();

        setContentView(R.layout.activity_cotizacion_servicio);
        recyclerArticles= findViewById(R.id.recicler_servicios_Articulos);
        recyclerPaquetes =findViewById(R.id.recicler_servicios_Paquetes);
        lunes=findViewById(R.id.checkBox_Lunes);
        martes=findViewById(R.id.checkBox_Martes);
        miercoles=findViewById(R.id.checkBox_Miercoles);
        jueves=findViewById(R.id.checkBox_Jueves);
        viernes=findViewById(R.id.checkBox_Viernes);
        sabado=findViewById(R.id.checkBox_Sabado);
        domingo=findViewById(R.id.checkBox_Domingo);
        name= findViewById(R.id.nombre);
        telefono= findViewById(R.id.telefonoProveedor);
        nombreProveedor= findViewById(R.id.nombre_p);
        direccion= findViewById(R.id.direccion_nuevoservicio);
        hora_inicio= findViewById(R.id.hora_inicio);
        hora_fin= findViewById(R.id.hora_final);

        servicioid=(int) getIntent().getSerializableExtra("id");
        Toast.makeText(this, "Numero servicio"+Integer.toString(servicioid), Toast.LENGTH_SHORT).show();
        retrofitInit();
        final Articulos articulos= new Articulos(servicioid);
        Call<ServicioResult> call = nixService.servicioId(articulos);
        call.enqueue(new Callback<ServicioResult>() {
            @Override
            public void onResponse(Call<ServicioResult> call, Response<ServicioResult> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CotizacionServicio.this, "Servicio obtenido correctamente", Toast.LENGTH_SHORT).show();
                    CatalogoServicios catalogoServicios= response.body().servicio;
                    name.setText(catalogoServicios.getNombre());
                    direccion.setText(catalogoServicios.getDireccion());
                    telefono.setText(catalogoServicios.getTelefono());
                    nombreProveedor.setText(catalogoServicios.getNombreProveedor());
                    hora_fin.setText(catalogoServicios.getHorarioCierre());
                    hora_inicio.setText(catalogoServicios.getHorarioApertura());
                    if (catalogoServicios.getLunes()==1){
                        lunes.setChecked(true);
                        lunes.setEnabled(false);
                    }
                    if (catalogoServicios.getMartes()==1){
                        martes.setChecked(true);
                        martes.setEnabled(false);
                    }
                    if (catalogoServicios.getMiercoles()==1){
                        miercoles.setChecked(true);
                        miercoles.setEnabled(false);
                    }
                    if (catalogoServicios.getJueves()==1){
                        jueves.setChecked(true);
                        jueves.setEnabled(false);
                    }
                    if (catalogoServicios.getViernes()==1){
                        viernes.setChecked(true);
                        viernes.setEnabled(false);
                    }
                    if (catalogoServicios.getSabado()==1){
                        sabado.setChecked(true);
                        sabado.setEnabled(false);
                    }
                    if (catalogoServicios.getDomingo()==1){
                        domingo.setChecked(true);
                        domingo.setEnabled(false);
                    }
                }
                else{
                    Toast.makeText(CotizacionServicio.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServicioResult> call, Throwable t) {

            }
        });
        Call<ArticulosListResult> callUno= nixService.articulosServicio(articulos);
        callUno.enqueue(new Callback<ArticulosListResult>() {
            @Override
            public void onResponse(Call<ArticulosListResult> call, Response<ArticulosListResult> response) {
                if (response.isSuccessful()){
                    articulosList= response.body().articulos;
                    articuloAdapter=  new ArticuloServicioRecyclerViewAdapter(articulosList, new OnListFragmentInteractionListener() {
                        @Override
                        public void onListFragmentInteraction(Articulos item) {
                            
                        }

                        @Override
                        public void onListFragmentInteraction(Paquetes mItem) {

                        }

                        @Override
                        public void onClick(View v) {

                        }

                        @Override
                        public void onClickAdd(Articulos mItem) {
                            Toast.makeText(CotizacionServicio.this, "Añadir articulo", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onClickSub(Articulos mItem) {
                            Toast.makeText(CotizacionServicio.this, "Quitar articulo", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onClickAdd(Paquetes mItem) {

                        }

                        @Override
                        public void onClickSub(Paquetes mItem) {

                        }
                    });
                    recyclerArticles.setAdapter(articuloAdapter);
                    recyclerArticles.setLayoutManager(new LinearLayoutManager(CotizacionServicio.this));
                }
                else{
                    Toast.makeText(CotizacionServicio.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                    Log.i("error",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ArticulosListResult> call, Throwable t) {

            }
        });
        Call<PaquetesListResult> callTres= nixService.paquetesServicio(articulos);
        callTres.enqueue(new Callback<PaquetesListResult>() {
            @Override
            public void onResponse(Call<PaquetesListResult> call, Response<PaquetesListResult> response) {
                if (response.isSuccessful()){
                    paquetesList= response.body().paquetes;
                    paqueteAdapter=  new PaqueteServicioRecyclerViewAdapter(paquetesList, new OnListFragmentInteractionListener() {
                        @Override
                        public void onListFragmentInteraction(Articulos item) {
                            
                        }

                        @Override
                        public void onListFragmentInteraction(Paquetes mItem) {

                        }

                        @Override
                        public void onClick(View v) {

                        }

                        @Override
                        public void onClickAdd(Articulos mItem) {
                            
                        }

                        @Override
                        public void onClickSub(Articulos mItem) {

                        }

                        @Override
                        public void onClickAdd(Paquetes mItem) {
                            Toast.makeText(CotizacionServicio.this, "Añadir paquete", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onClickSub(Paquetes mItem) {
                            Toast.makeText(CotizacionServicio.this, "Quitar paquete", Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerPaquetes.setAdapter(paqueteAdapter);
                    recyclerPaquetes.setLayoutManager(new LinearLayoutManager(CotizacionServicio.this));
                }
                else{
                    Toast.makeText(CotizacionServicio.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                    Log.i("error",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<PaquetesListResult> call, Throwable t) {

            }
        });
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Articulos item);
        void onListFragmentInteraction(Paquetes mItem);
        void onClick(View v);

        void onClickAdd(Articulos mItem);
        void onClickSub(Articulos mItem);


        void onClickAdd(Paquetes mItem);
        void onClickSub(Paquetes mItem);
    }

}
