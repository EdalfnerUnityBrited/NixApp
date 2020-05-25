package com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.DB.ZonaServicio;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.BusquedaServicios.ArticuloServicioRecyclerViewAdapter;
import com.example.nixapp.UI.usuario.misEventos.BusquedaServicios.PaqueteServicioRecyclerViewAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ArticulosListResult;
import com.example.nixapp.conn.results.PaquetesListResult;
import com.example.nixapp.conn.results.ServicioResult;
import com.example.nixapp.conn.results.ZonaListResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CotizacionServicio extends AppCompatActivity {

    class ArticulosFinales
    {
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCantidad() {
            return Cantidad;
        }

        public void setCantidad(int cantidad) {
            Cantidad = cantidad;
        }

        int id;
        int Cantidad;
        public ArticulosFinales(int id,int Cantidad)
        {
            this.id = id;
            this.Cantidad = Cantidad;
        }
    }


    List<ArticulosFinales> articulosFinales = new ArrayList<>();
    List<ArticulosFinales> paquetesFinales = new ArrayList<>();
    int servicioid;
    Button guardar_Cotizacion;
    CheckBox lunes, martes, miercoles, jueves, viernes, sabado, domingo;
    Spinner categorias;
    NixService nixService;
    NixClient nixClient;
    TextView hora_inicio,hora_fin,direccion, name, telefono, nombreProveedor, precioTotal;
    TextView municipios;
    List<Articulos> articulosList;
    List<Paquetes> paquetesList;
    ArticuloServicioRecyclerViewAdapter articuloAdapter;
    PaqueteServicioRecyclerViewAdapter paqueteAdapter;
    int precios;
    RecyclerView recyclerArticles, recyclerPaquetes;
    private EventosAdapter mAdapter;
    private ArrayList<EventosItems> mEventsList;
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
        municipios = findViewById(R.id.municipios_nombres);
        categorias = findViewById(R.id.spinnerSimple);
        precioTotal = findViewById(R.id.precioTotal);
        initList();

        mAdapter = new EventosAdapter(this, mEventsList);
        categorias.setAdapter(mAdapter);

        servicioid=(int) getIntent().getSerializableExtra("id");
        guardar_Cotizacion = findViewById(R.id.servicioGuardado);
        guardar_Cotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Articulos = "";
                int contador = 0;
                Articulos chequear = null;
                Paquetes chequeo = null;

                for (ArticulosFinales art: articulosFinales)
                {
                    for (Articulos ar: articulosList)
                    {
                        if(ar.getId() == art.id)
                        {
                            chequear = articulosList.get(contador);
                            Articulos+="Articulo: "+art.id + " " + chequear.getNombre() + " " + art.Cantidad + "\n";
                        }
                        contador++;
                    }
                    contador = 0;

                }
                for (ArticulosFinales art: paquetesFinales)
                {
                    for (Paquetes ar: paquetesList)
                    {
                        if(ar.getId() == art.id)
                        {
                            chequeo = paquetesList.get(contador);
                            Articulos+= "Paquete: "+art.id + " " + chequeo.getNombre() + " " + art.Cantidad + "\n";
                        }
                        contador++;
                    }
                    contador = 0;
                }

                Toast.makeText(getApplicationContext(), Articulos, Toast.LENGTH_LONG).show();
            }
        });

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
                    categorias.setSelection(catalogoServicios.getCategoriaevento());
                    categorias.setEnabled(false);
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
                Call<ZonaListResult> call4 = nixService.municipiosServicio(articulos);
                call4.enqueue(new Callback<ZonaListResult>() {
                    @Override
                    public void onResponse(Call<ZonaListResult> call, Response<ZonaListResult> response) {

                        if(response.isSuccessful())
                        {
                            List<ZonaServicio> municipiosTotales = response.body().municipios;
                            String municip = "";
                            for (ZonaServicio mun: municipiosTotales)
                            {
                                municip += mun.getMunicipio() + "\n";

                            }
                            municipios.setText(municip);
                        }
                        else
                        {
                            Toast.makeText(CotizacionServicio.this, "Error en los municipios", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ZonaListResult> call, Throwable t) {
                        Toast.makeText(CotizacionServicio.this, "Error en los municipios desde la llamada", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ServicioResult> call, Throwable t) {
                Toast.makeText(CotizacionServicio.this, "Error en los datos desde la llamada", Toast.LENGTH_SHORT).show();
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
                            String digito = articuloAdapter.numeros();
                            int digitos = Integer.valueOf(digito);
                            String costo = String.valueOf(precioTotal.getText());
                            precios = Integer.parseInt(costo) + Integer.parseInt(mItem.getPrecio());
                            precioTotal.setText(String.valueOf(precios));
                            int contador = 0,con = 0;
                            boolean ya_esta = false;
                            ArticulosFinales nuevo = new ArticulosFinales(mItem.getId(),digitos);
                            for (ArticulosFinales art: articulosFinales) {

                                if(art.id == mItem.getId())
                                {
                                    ya_esta = true;
                                    con = contador;
                                }
                                contador++;
                            }
                            if(ya_esta == false)
                            {
                                articulosFinales.add(nuevo);
                            }
                            else
                            {
                                articulosFinales.set(con,nuevo);
                            }

                        }

                        @Override
                        public void onClickSub(Articulos mItem) {
                            String digito = articuloAdapter.numeros();
                            int contador = 0,con = 0;
                            int digitos = Integer.valueOf(digito);
                            if(!digito.equals("0"))
                            {
                                String costo = String.valueOf(precioTotal.getText());
                                precios = Integer.parseInt(costo) - Integer.parseInt(mItem.getPrecio());
                                precioTotal.setText(String.valueOf(precios));
                                ArticulosFinales nuevo = new ArticulosFinales(mItem.getId(),digitos-1);
                                for (ArticulosFinales art: articulosFinales) {

                                    if(art.id == mItem.getId())
                                    {
                                        con = contador;
                                    }
                                    contador++;
                                }

                                if(digitos==1)
                                {
                                    articulosFinales.remove(con);
                                }
                                else
                                {
                                    articulosFinales.set(con,nuevo);
                                }
                            }

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
                            String digito = paqueteAdapter.numeros();
                            int digitos = Integer.valueOf(digito);
                            String costo = String.valueOf(precioTotal.getText());
                            precios = Integer.parseInt(costo) + Integer.parseInt(mItem.getPrecio());
                            precioTotal.setText(String.valueOf(precios));
                            int contador = 0,con = 0;
                            boolean ya_esta = false;
                            ArticulosFinales nuevo = new ArticulosFinales(mItem.getId(),digitos);
                            for (ArticulosFinales art: paquetesFinales) {

                                if(art.id == mItem.getId())
                                {
                                    ya_esta = true;
                                    con = contador;
                                }
                                contador++;
                            }
                            if(ya_esta == false)
                            {
                                paquetesFinales.add(nuevo);
                            }
                            else
                            {
                                paquetesFinales.set(con,nuevo);
                            }
                        }

                        @Override
                        public void onClickSub(Paquetes mItem) {
                            String digito = paqueteAdapter.numeros();
                            int contador = 0,con = 0;
                            int digitos = Integer.valueOf(digito);
                            if(!digito.equals("0"))
                            {
                                String costo = String.valueOf(precioTotal.getText());
                                precios = Integer.parseInt(costo) - Integer.parseInt(mItem.getPrecio());
                                precioTotal.setText(String.valueOf(precios));
                                ArticulosFinales nuevo = new ArticulosFinales(mItem.getId(),digitos-1);
                                for (ArticulosFinales art: paquetesFinales) {

                                    if(art.id == mItem.getId())
                                    {
                                        con = contador;
                                    }
                                    contador++;
                                }

                                if(digitos==1)
                                {
                                    paquetesFinales.remove(con);
                                }
                                else
                                {
                                    paquetesFinales.set(con,nuevo);
                                }
                            }
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
