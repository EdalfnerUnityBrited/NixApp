package com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Cotizacion;
import com.example.nixapp.DB.CotizacionArticulo;
import com.example.nixapp.DB.CotizacionPaquete;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.HorarioVerificar;
import com.example.nixapp.DB.PaqueteArticulo;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.DB.ZonaServicio;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.BusquedaServicios.ArticuloServicioRecyclerViewAdapter;
import com.example.nixapp.UI.usuario.misEventos.BusquedaServicios.PaqueteServicioRecyclerViewAdapter;
import com.example.nixapp.UI.usuario.misEventos.EditarEvento;
import com.example.nixapp.UI.usuario.misEventos.EventosAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ArticulosListResult;
import com.example.nixapp.conn.results.ArticulosPaqueteResult;
import com.example.nixapp.conn.results.CotizacionArticuloResult;
import com.example.nixapp.conn.results.CotizacionPaqueteResult;
import com.example.nixapp.conn.results.CotizacionResult;
import com.example.nixapp.conn.results.EventosResult;
import com.example.nixapp.conn.results.PaquetesListResult;
import com.example.nixapp.conn.results.ServicioResult;
import com.example.nixapp.conn.results.ZonaListResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
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

        int id;
        int Cantidad;
        String nombre;
        public ArticulosFinales(int id,int Cantidad,String nombre)
        {
            this.id = id;
            this.Cantidad = Cantidad;
            this.nombre = nombre;
        }
    }

    boolean ZonaPermitida = false,horarioPermitido = false;
    Cotizacion coti;
    List<ArticulosFinales> articulosFinales = new ArrayList<>();
    List<ArticulosFinales> paquetesFinales = new ArrayList<>();
    List<CotizacionArticulo> articulosEnCotizacion = new ArrayList<>();
    List<CotizacionPaquete> paquetesEnCotizacion = new ArrayList<>();
    int servicioid;
    public static int idEvento;
    public static String costoTotal, nombreServicio, nombreProvee, idService;
    Button guardar_Cotizacion, servicio_contratado, agregar_articulos;
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
    int precios, id_evento;
    RecyclerView recyclerArticles, recyclerPaquetes;
    private EventosAdapter mAdapter;
    private ArrayList<EventosItems> mEventsList;
    boolean agregados = false;
    List<ZonaServicio> municipiosTotales;
    String id_cot = "";
    public static String desglose = "";
    Eventos eventos;
    CatalogoServicios catalogoServicios;
    AlertDialog.Builder informacion,resumen_compra;
    RatingBar estrellas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizacion_servicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backarrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        articulosList= new ArrayList<>();
        paquetesList= new ArrayList<>();
        retrofitInit();
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
        guardar_Cotizacion = findViewById(R.id.servicioGuardado);
        servicio_contratado = findViewById(R.id.servicioContratado);
        agregar_articulos = findViewById(R.id.agregarArticulos);


        mAdapter = new EventosAdapter(this, mEventsList);
        categorias.setAdapter(mAdapter);
        servicioid=(int) getIntent().getSerializableExtra("id");
        idService= Integer.toString(servicioid);
        try {
            id_evento =(int) getIntent().getSerializableExtra("id_Evento");
            idEvento = id_evento;
                    Busqueda busqueda = new Busqueda(String.valueOf(id_evento));
            Call<EventosResult> llamadaEvento  = nixService.buscarEventoId(busqueda);
            llamadaEvento.enqueue(new Callback<EventosResult>() {
                @Override
                public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                    if(response.isSuccessful())
                    {
                        eventos = response.body().eventos;
                        HorarioVerificar horaV = new HorarioVerificar(servicioid,eventos.getFecha(),eventos.getHora());
                        Call<ResponseBody> llamadaHorario = nixService.fechaVerificada(horaV);
                        llamadaHorario.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful())
                                {
                                    horarioPermitido = true;
                                }
                                else
                                {
                                    horarioPermitido = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(CotizacionServicio.this, "Error al verificar Horiario", Toast.LENGTH_SHORT).show();
                            }
                        });
                        servicio_contratado.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (ZonaServicio mun: municipiosTotales) {
                                    if(mun.getMunicipio().equals(eventos.getMunicipio()))
                                    {
                                        ZonaPermitida = true;
                                    }

                                }
                                if(ZonaPermitida == true)
                                {
                                    String date = eventos.getFecha();
                                    String[] dateS = date.split("-");
                                    Calendar calendar = Calendar.getInstance();
                                    Date fechaEvento = null;
                                    int dia_semana = 177;
                                    try {
                                        fechaEvento = new SimpleDateFormat("dd/MM/yyyy").parse(dateS[2]+ "/"+dateS[1]+"/"+dateS[0]);
                                        calendar.setTime(fechaEvento);
                                        dia_semana = calendar.get(Calendar.DAY_OF_WEEK);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(),"Error al cambiar fecha",Toast.LENGTH_LONG).show();
                                    }

                                    if(((dia_semana == 1 && catalogoServicios.getDomingo() == 1))||(dia_semana == 2 && catalogoServicios.getLunes() == 1)||(dia_semana == 3 && catalogoServicios.getMartes() == 1)||(dia_semana == 4 && catalogoServicios.getMiercoles() == 1)||(dia_semana == 5 && catalogoServicios.getJueves() == 1)||(dia_semana == 6 && catalogoServicios.getViernes() == 1)||(dia_semana == 7 && catalogoServicios.getSabado() == 1))
                                    {
                                        if(horarioPermitido == true)
                                        {
                                            String todaInfo = "";
                                            costoTotal= precioTotal.getText().toString();
                                            if(!costoTotal.equals("00"))
                                            {
                                                for (ArticulosFinales artic: articulosFinales) {

                                                    todaInfo += "Articulo: " + artic.Cantidad + " " + artic.nombre + "\n";

                                                }
                                                for (ArticulosFinales artic: paquetesFinales) {

                                                    todaInfo += "Paquete: " + artic.Cantidad + " " + artic.nombre + "\n";

                                                }
                                                todaInfo += "Total a pagar : $"+costoTotal+ " MXN" ;
                                                desglose = todaInfo;
                                                resumen_compra = new AlertDialog.Builder(CotizacionServicio.this);
                                                resumen_compra.setTitle("Resumen de la compra:");
                                                resumen_compra.setMessage("Articulos que se cobraran:" + "\n" + todaInfo);
                                                resumen_compra.setCancelable(false);
                                                resumen_compra.setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogo1, int id) {
                                                        nombreServicio= name.getText().toString();
                                                        nombreProvee= nombreProveedor.getText().toString();
                                                        Intent intent= new Intent(CotizacionServicio.this,EleccionPago.class);
                                                        intent.putExtra("Pago", 1);
                                                        intent.putExtra("total del pago", costoTotal);
                                                        intent.putExtra("id_cotizacion", String.valueOf(catalogoServicios.getId()));
                                                        startActivity(intent);
                                                    }
                                                });
                                                resumen_compra.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                resumen_compra.show();
                                            }
                                            else
                                            {
                                                Toast.makeText(CotizacionServicio.this, "Debes agregar algun producto para hacer la compra", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                        else
                                        {
                                            Toast.makeText(CotizacionServicio.this, "Este Servicio tiene esa fecha ocupada, lo sentimos", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"No se trabaja ese dia, lo sentimos",Toast.LENGTH_LONG).show();
                                    }


                                }
                                else
                                {
                                    Toast.makeText(CotizacionServicio.this, "Este Servicio no esta disponible en tu region, lo sentimos", Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(CotizacionServicio.this, "Evento no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EventosResult> call, Throwable t) {
                    Toast.makeText(CotizacionServicio.this, "Error al buscar el evento", Toast.LENGTH_SHORT).show();
                }
            });


            id_cot =(String) getIntent().getSerializableExtra("id_cotizacion");
            guardar_Cotizacion.setVisibility(View.GONE);
            agregar_articulos.setVisibility(View.VISIBLE);
            final Articulos arti= new Articulos(Integer.valueOf(id_cot));
            Call<CotizacionArticuloResult> llamada = nixService.articulosEnCotizacion(arti);
            llamada.enqueue(new Callback<CotizacionArticuloResult>() {
                @Override
                public void onResponse(Call<CotizacionArticuloResult> call, Response<CotizacionArticuloResult> response) {
                    if(response.isSuccessful())
                    {
                        articulosEnCotizacion = response.body().articulos;
                        Call<CotizacionPaqueteResult> llamada2 = nixService.paquetesEnCotizacion(arti);
                        llamada2.enqueue(new Callback<CotizacionPaqueteResult>() {
                            @Override
                            public void onResponse(Call<CotizacionPaqueteResult> call, Response<CotizacionPaqueteResult> response) {
                                if(response.isSuccessful())
                                {
                                    paquetesEnCotizacion = response.body().paquetes;
                                    agregar_articulos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            List<ArticuloServicioRecyclerViewAdapter.ViewHolder> vistas = articuloAdapter.getVistas();
                                            List<PaqueteServicioRecyclerViewAdapter.ViewHolder> paquetvistas = paqueteAdapter.getVistas();
                                            if(agregados == false)
                                            {
                                                String nombres = "";
                                                int precio = 0;
                                                for (CotizacionArticulo articul: articulosEnCotizacion) {
                                                    for ( ArticuloServicioRecyclerViewAdapter.ViewHolder vis: vistas)
                                                    {
                                                        if(articul.getId_articulo() == vis.mItem.getId())
                                                        {
                                                            nombres+= vis.mItem.getNombre() + " "+articul.getCantidad()+"\n";
                                                            vis.cantidad.setText(articul.getCantidad());
                                                            precio = precio + (Integer.valueOf(vis.mItem.getPrecio()) * Integer.valueOf(articul.getCantidad()));
                                                            ArticulosFinales nuevo = new ArticulosFinales(articul.getId_articulo(),Integer.valueOf(articul.getCantidad()),vis.mItem.getNombre());
                                                            articulosFinales.add(nuevo);
                                                        }
                                                    }
                                                }
                                                for (CotizacionPaquete paq: paquetesEnCotizacion)
                                                {
                                                    for (PaqueteServicioRecyclerViewAdapter.ViewHolder vista:paquetvistas)
                                                    {
                                                        if(paq.getId_paquete() == vista.mItem.getId())
                                                        {
                                                            vista.cantidad.setText(paq.getCantidad());
                                                            precio = precio + (Integer.valueOf(vista.mItem.getPrecio())* Integer.valueOf(paq.getCantidad()));
                                                            ArticulosFinales nuevo = new ArticulosFinales(paq.getId_paquete(),Integer.valueOf(paq.getCantidad()),vista.mItem.getNombre());
                                                            paquetesFinales.add(nuevo);
                                                        }
                                                    }
                                                }

                                                agregados = true;
                                                precioTotal.setText(String.valueOf(precio));
                                                Toast.makeText(CotizacionServicio.this,String.valueOf(precio), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(CotizacionServicio.this, "No se encontraron paquetes de cotizacion", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<CotizacionPaqueteResult> call, Throwable t) {
                                Toast.makeText(CotizacionServicio.this, "Error en la obtencion de paquetes de Cotizacion", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(CotizacionServicio.this, "No se encontraron articulos de cotizacion", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<CotizacionArticuloResult> call, Throwable t) {
                    Toast.makeText(CotizacionServicio.this, "Error en la obtencion de articulos de Cotizacion", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e)
        {
            id_cot = "";
        }

        guardar_Cotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String total= precioTotal.getText().toString();
                if(!total.equals("00"))
                {
                    final Cotizacion cotizacion= new Cotizacion(total, servicioid, EditarEvento.id_evento);
                    Call<CotizacionResult> callCot = nixService.guardarCotizacion(cotizacion);
                    callCot.enqueue(new Callback<CotizacionResult>() {
                        @Override
                        public void onResponse(Call<CotizacionResult> call, Response<CotizacionResult> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(CotizacionServicio.this, "Guardada correctamente", Toast.LENGTH_SHORT).show();
                                coti= response.body().cotizacion;
                                List<CotizacionArticulo> cotizacionArticuloList= new ArrayList<>();
                                List<CotizacionPaquete> cotizacionPaqueteList= new ArrayList<>();
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
                                            CotizacionArticulo cotiart= new CotizacionArticulo(Integer.toString(art.Cantidad), Integer.parseInt(coti.getId()), art.id);
                                            cotizacionArticuloList.add(cotiart);
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
                                            CotizacionPaquete cotipaq= new CotizacionPaquete(Integer.toString(art.Cantidad), Integer.parseInt(coti.getId()), art.id);
                                            cotizacionPaqueteList.add(cotipaq);
                                            Articulos+= "Paquete: "+art.id + " " + chequeo.getNombre() + " " + art.Cantidad + "\n";
                                        }
                                        contador++;
                                    }
                                    contador = 0;
                                }
                                Call<ResponseBody> callArt= nixService.articulosCotización(cotizacionArticuloList);
                                callArt.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(CotizacionServicio.this, "Articulos añadidoa", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(CotizacionServicio.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                Call<ResponseBody> callPaq = nixService.paquetesCotizacion(cotizacionPaqueteList);
                                callPaq.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(CotizacionServicio.this, "Paqutes añadidos", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(CotizacionServicio.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(CotizacionServicio.this, "Error en la llamada", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(getApplicationContext(), Articulos, Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(CotizacionServicio.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                Log.i("error", response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<CotizacionResult> call, Throwable t) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(CotizacionServicio.this, "No puedes guardar una cotizacion vacia", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Toast.makeText(this, "Numero evento :"+Integer.toString(EditarEvento.id_evento), Toast.LENGTH_SHORT).show();
        retrofitInit();
        final Articulos articulos= new Articulos(servicioid);
        Call<ServicioResult> call = nixService.servicioId(articulos);
        call.enqueue(new Callback<ServicioResult>() {
            @Override
            public void onResponse(Call<ServicioResult> call, Response<ServicioResult> response) {
                if (response.isSuccessful()){
                    //Toast.makeText(CotizacionServicio.this, "Servicio obtenido correctamente", Toast.LENGTH_SHORT).show();
                    catalogoServicios= response.body().servicio;
                    estrellas = findViewById(R.id.ratingestrellas);
                    name.setText(catalogoServicios.getNombre());
                    direccion.setText(catalogoServicios.getDireccion());
                    telefono.setText(catalogoServicios.getTelefono());
                    nombreProveedor.setText(catalogoServicios.getNombreProveedor());
                    hora_fin.setText(catalogoServicios.getHorarioCierre());
                    hora_inicio.setText(catalogoServicios.getHorarioApertura());
                    categorias.setSelection(catalogoServicios.getCategoriaevento());
                    categorias.setEnabled(false);
                    estrellas.setRating(Float.valueOf(catalogoServicios.getCalificacion()));
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
                            municipiosTotales = response.body().municipios;
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
                            ArticulosFinales nuevo = new ArticulosFinales(mItem.getId(),digitos,mItem.getNombre());
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
                                ArticulosFinales nuevo = new ArticulosFinales(mItem.getId(),digitos-1,mItem.getNombre());
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

                        @Override
                        public void onInfo(Paquetes mItem) {

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
                            ArticulosFinales nuevo = new ArticulosFinales(mItem.getId(),digitos,mItem.getNombre());
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
                                ArticulosFinales nuevo = new ArticulosFinales(mItem.getId(),digitos-1,mItem.getNombre());
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

                        @Override
                        public void onInfo(Paquetes mItem) {
                            Articulos contenido = new Articulos(mItem.getId());
                            Call<ArticulosPaqueteResult> llamadaDeContenido = nixService.articulosEnPaquete(contenido);
                            llamadaDeContenido.enqueue(new Callback<ArticulosPaqueteResult>() {
                                @Override
                                public void onResponse(Call<ArticulosPaqueteResult> call, Response<ArticulosPaqueteResult> response) {
                                    if(response.isSuccessful())
                                    {
                                        List<PaqueteArticulo> contenidoArt = response.body().paqueteArt;
                                        String todos = "";
                                        for (PaqueteArticulo paq1: contenidoArt)
                                        {
                                            todos += paq1.getArticulo() + "\n";
                                        }
                                        informacion = new AlertDialog.Builder(CotizacionServicio.this);
                                        informacion.setTitle("Informacion del paquete:");
                                        informacion.setMessage("Articulos que incluye:" + "\n" + todos);
                                        informacion.setCancelable(false);
                                        informacion.setPositiveButton("Leido", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogo1, int id) {

                                            }
                                        });
                                        informacion.show();

                                    }
                                    else
                                    {
                                        Toast.makeText(CotizacionServicio.this, "Paquete no encontrado/sin contenido", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ArticulosPaqueteResult> call, Throwable t) {
                                    Toast.makeText(CotizacionServicio.this, "Error al obtener articulos del paquete", Toast.LENGTH_SHORT).show();
                                }
                            });
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

        void onInfo(Paquetes mItem);
    }

}
