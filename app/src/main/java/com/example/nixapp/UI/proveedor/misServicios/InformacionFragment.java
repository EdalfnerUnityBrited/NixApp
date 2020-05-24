package com.example.nixapp.UI.proveedor.misServicios;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.ZonaServicio;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.EventosAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ServicioResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class InformacionFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private CatalogoServicios catalogoServicio;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    Spinner spinner , spinners;
    List<ZonaServicio> zonaServicios = new ArrayList<>();
    String[] Minicipios = new String[]{
            "Elige un municipio", "Acatic", "Ameca", "Arandas", "Atotonilco el alto", "Chapala", "Cocula", "El Arenal", "El Salto", "Guachinango", "Guadalajara",
            "Jocotepec", "La Barca", "Lagos de Moreno", "Mascota", "Mazamitla", "Mezquitic", "Puerto Vallarta", "San Juan de los Lagos", "Tlaquepaque", "Sayula",
            "Tala", "Tapalpa", "Tequila", "Tlajomulco de Zuñiga", "Tonala", "Tototlan", "Zapopan", "Zapotlanejo"
    };
    String municipio = "", clickedName;
    int categoria_evento;
    EditText hora_inicio,hora_fin,direccion, name, telefono, nombreProveedor;
    TextView municipios;
    TimePickerDialog hora_inicial,hora_final;
    Button agregar_mun,buscar_direccion, crear;
    private EventosAdapter mAdapter;
    private ArrayList<EventosItems> mEventsList;
    List<String> municipiosAgregados= new ArrayList<>();
    CheckBox lunes, martes, miercoles, jueves, viernes, sabado, domingo;
    NixService nixService;
    NixClient nixClient;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infoservicio_proveedor,container,false);
        retrofitInit();
        mProgressDialog= new ProgressDialog(getContext());
        lunes=view.findViewById(R.id.checkBox_Lunes);
        martes=view.findViewById(R.id.checkBox_Martes);
        miercoles=view.findViewById(R.id.checkBox_Miercoles);
        jueves=view.findViewById(R.id.checkBox_Jueves);
        viernes=view.findViewById(R.id.checkBox_Viernes);
        sabado=view.findViewById(R.id.checkBox_Sabado);
        domingo=view.findViewById(R.id.checkBox_Domingo);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_servicio);
        mapFragment.getMapAsync(this);
        spinner = view.findViewById(R.id.spinner1);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.texto_municipios,Minicipios
        );
        name= view.findViewById(R.id.nombre);
        telefono= view.findViewById(R.id.telefonoProveedor);
        nombreProveedor= view.findViewById(R.id.nombre_p);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                municipio = spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                municipio = spinner.getSelectedItem().toString();
            }
        });
        spinners = view.findViewById(R.id.spinnerSimple);
        initList();
        crear= view.findViewById(R.id.servicioTerminado);
        mAdapter = new EventosAdapter(getActivity(), mEventsList);
        spinners.setAdapter(mAdapter);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int monday=0;
                int tuesday=0;
                int wednesday=0;
                int thursday=0;
                int friday=0;
                int saturday=0;
                int sunday=0;

                String nombreServicio=name.getText().toString();
                String direccionServicio=direccion.getText().toString();
                String telefonoServicio= telefono.getText().toString();
                String nombreProvee= nombreProveedor.getText().toString();
                if (lunes.isChecked()){
                    monday=1;
                }
                if (martes.isChecked()){
                    tuesday=1;
                }
                if (miercoles.isChecked()){
                    wednesday=1;
                }
                if (jueves.isChecked()){
                    thursday=1;
                }
                if (viernes.isChecked()){
                    friday=1;
                }
                if (sabado.isChecked()){
                    saturday=1;
                }
                if (domingo.isChecked()){
                    sunday=1;
                }
                if (CrearServicioMenu.servicio==0){
                    String horaInicio=hora_inicio.getText().toString()+":00";
                    String horaFin=hora_fin.getText().toString()+":00";
                    if (horaInicio.compareTo(horaFin)>0){
                        Toast.makeText(getActivity(), "Si jala", Toast.LENGTH_SHORT).show();
                    }else{
                        mProgressDialog.setTitle("Creando servicio...");
                        mProgressDialog.setMessage("Por favor espere");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show();
                        CatalogoServicios catalogoServicios= new CatalogoServicios(nombreServicio, direccionServicio, telefonoServicio, horaInicio, horaFin, "",categoria_evento, monday, tuesday, wednesday, thursday, friday, saturday, sunday, nombreProvee);
                        Call<ServicioResult> call = nixService.crearServicio(catalogoServicios);
                        call.enqueue(new Callback<ServicioResult>() {
                            @Override
                            public void onResponse(Call<ServicioResult> call, Response<ServicioResult> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(getActivity(), "Creado correctamente", Toast.LENGTH_SHORT).show();
                                    catalogoServicio=response.body().servicio;
                                    if (!municipiosAgregados.isEmpty()){
                                        for(String mun : municipiosAgregados){
                                            ZonaServicio zonaServicio= new ZonaServicio(catalogoServicio.getId(),mun);
                                            zonaServicios.add(zonaServicio);

                                        }
                                        Call<ResponseBody> callDos = nixService.munServicio(zonaServicios);
                                        callDos.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()){
                                                    Toast.makeText(getActivity(), "Actalizada zona de servicio", Toast.LENGTH_SHORT).show();
                                                    CrearServicioMenu.servicio= catalogoServicio.getId();
                                                }
                                                else{
                                                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                                                    Log.i("error",response.errorBody().toString());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });

                                    }
                                }
                                else{
                                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                                    Log.i("error", response.errorBody().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<ServicioResult> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error en la ruta", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    mProgressDialog.dismiss();
                }
                else{
                    String horaInicio=hora_inicio.getText().toString();
                    String horaFin=hora_fin.getText().toString();
                    if (horaInicio.compareTo(horaFin)>0){
                        Toast.makeText(getActivity(), "Si jala", Toast.LENGTH_SHORT).show();
                    }else{
                        mProgressDialog.setTitle("Actualizando servicio...");
                        mProgressDialog.setMessage("Por favor espere");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show();
                        CatalogoServicios catalogoServicios= new CatalogoServicios(nombreServicio, direccionServicio, telefonoServicio, horaInicio, horaFin, nombreProvee,categoria_evento, monday, tuesday, wednesday, thursday, friday, saturday, sunday, CrearServicioMenu.servicio);
                        Call<ServicioResult> call = nixService.actualizarServicio(catalogoServicios);
                        call.enqueue(new Callback<ServicioResult>() {
                            @Override
                            public void onResponse(Call<ServicioResult> call, Response<ServicioResult> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(getActivity(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    catalogoServicio=response.body().servicio;
                                    if (!municipiosAgregados.isEmpty()){
                                        for(String mun : municipiosAgregados){
                                            ZonaServicio zonaServicio= new ZonaServicio(catalogoServicio.getId(),mun);
                                            zonaServicios.add(zonaServicio);

                                        }
                                        Call<ResponseBody> callDos = nixService.munServicio(zonaServicios);
                                        callDos.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()){
                                                    Toast.makeText(getActivity(), "Actalizada zona de servicio", Toast.LENGTH_SHORT).show();
                                                    CrearServicioMenu.servicio= catalogoServicio.getId();
                                                }
                                                else{
                                                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                                                    Log.i("error",response.errorBody().toString());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });

                                    }
                                }
                                else{
                                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                                    Log.i("error", response.errorBody().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<ServicioResult> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error en la ruta", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    mProgressDialog.dismiss();
                }
            }
        });

        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventosItems clickedItem = (EventosItems) parent.getItemAtPosition(position);
                categoria_evento= position;
                clickedName = clickedItem.getEventoName();
                Toast.makeText(getActivity(), clickedName + " seleccionada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        hora_inicio = view.findViewById(R.id.hora_inicio);
        hora_inicio.setInputType(InputType.TYPE_NULL);
        hora_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale2 = getResources().getConfiguration().locale;
                Locale.setDefault(locale2);
                final Calendar cldr2 = Calendar.getInstance();
                final int hour = cldr2.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr2.get(Calendar.MINUTE);
                // time picker dialog
                hora_inicial = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String minutos;
                                String horas;
                                if(sMinute < 10) {
                                    minutos = "0" + String.valueOf(sMinute);
                                    hora_inicio.setText(sHour + ":" + minutos);

                                }
                                else if(sHour < 10)
                                {
                                    horas = "0" + String.valueOf(sHour);
                                    hora_inicio.setText(horas + ":" + sMinute);
                                }
                                else if(sHour <0 && sMinute< 0)
                                {
                                    minutos = "0" + String.valueOf(sMinute);
                                    horas = "0" + String.valueOf(sHour);
                                    hora_inicio.setText(horas + ":" + minutos);
                                }
                                else
                                {
                                    hora_inicio.setText(sHour + ":" + sMinute);
                                }

                            }
                        }, hour, minutes, true);
                hora_inicial.show();
            }
        });
        hora_fin = view.findViewById(R.id.hora_final);
        hora_fin.setInputType(InputType.TYPE_NULL);
        hora_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale2 = getResources().getConfiguration().locale;
                Locale.setDefault(locale2);
                final Calendar cldr2 = Calendar.getInstance();
                int hour = cldr2.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr2.get(Calendar.MINUTE);
                // time picker dialog
                hora_final = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String minutos;
                                String horas;
                                if(sMinute < 10) {
                                    minutos = "0" + String.valueOf(sMinute);
                                    hora_fin.setText(sHour + ":" + minutos);

                                }
                                else if(sHour < 10)
                                {
                                    horas = "0" + String.valueOf(sHour);
                                    hora_fin.setText(horas + ":" + sMinute);
                                }
                                else if(sHour <0 && sMinute< 0)
                                {
                                    minutos = "0" + String.valueOf(sMinute);
                                    horas = "0" + String.valueOf(sHour);
                                    hora_fin.setText(horas + ":" + minutos);
                                }
                                else
                                {
                                    hora_fin.setText(sHour + ":" + sMinute);
                                }
                            }
                        }, hour, minutes, true);
                hora_final.show();
            }
        });
        municipios = view.findViewById(R.id.municipios_mostrar);
        agregar_mun = view.findViewById(R.id.otro_municipio);
        agregar_mun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!spinner.getSelectedItem().toString().equals("Elige un municipio"))
                {
                    String municipios_elegidos = municipios.getText().toString();
                    boolean ya_se_agrego = false;
                    String[] separados = municipios_elegidos.split("\n");
                    for (String nombres : separados)
                    {
                        if(nombres.equals(spinner.getSelectedItem().toString()))
                        {
                            ya_se_agrego = true;
                        }
                    }
                    if(ya_se_agrego == false)
                    {
                        municipios.setText(municipios.getText()+"\n"+ spinner.getSelectedItem().toString());
                        municipiosAgregados.add(spinner.getSelectedItem().toString());

                    }
                    else
                    {
                        ya_se_agrego = false;
                    }

                }

            }
        });
        direccion = (EditText) view.findViewById(R.id.direccion_nuevoservicio);
        buscar_direccion = (Button) view.findViewById(R.id.buscar_direccion);
        buscar_direccion.setOnClickListener(this);
       if (CrearServicioMenu.servicio!=0){
           Articulos articulos= new Articulos(CrearServicioMenu.servicio);
           Call<ServicioResult> call = nixService.servicioId(articulos);
           call.enqueue(new Callback<ServicioResult>() {
               @Override
               public void onResponse(Call<ServicioResult> call, Response<ServicioResult> response) {
                   if (response.isSuccessful()){
                       Toast.makeText(getActivity(), "Servicio obtenido correctamente", Toast.LENGTH_SHORT).show();
                       CatalogoServicios catalogoServicios= response.body().servicio;
                       name.setText(catalogoServicios.getNombre());
                       direccion.setText(catalogoServicios.getDireccion());
                       telefono.setText(catalogoServicios.getTelefono());
                       nombreProveedor.setText(catalogoServicios.getNombreProveedor());
                       if (catalogoServicios.getLunes()==1){
                           lunes.setChecked(true);
                       }
                       if (catalogoServicios.getMartes()==1){
                           martes.setChecked(true);
                       }
                       if (catalogoServicios.getMiercoles()==1){
                           miercoles.setChecked(true);
                       }
                       if (catalogoServicios.getJueves()==1){
                           jueves.setChecked(true);
                       }
                       if (catalogoServicios.getViernes()==1){
                           viernes.setChecked(true);
                       }
                       if (catalogoServicios.getSabado()==1){
                           sabado.setChecked(true);
                       }
                       if (catalogoServicios.getDomingo()==1){
                           domingo.setChecked(true);
                       }
                   }
                   else{
                       Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onFailure(Call<ServicioResult> call, Throwable t) {

               }
           });
       }
        return view;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
        //hacer zoom al usuario
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(15)
                    .bearing(0)
                    .tilt(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "Nix necesita conocer tu ubicación", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private void ponerDireccionEnMapa(String direccion){
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?&address=%1$s+Jalisco+Mexico&key=AIzaSyAPGGYxsJfpi3DY0o11lAR4-Gccfpf3juw",direccion);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Toast.makeText(getActivity(), "ERROR API GEOCODING", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            double ltEvento, lnEvento;
                            String[] ltEvento1Json = myResponse.split("\"lat\"+ +:+ ");
                            String[] lnEvento1Json = myResponse.split("\"lng\"+ +:+ ");
                            String[] ltEventoFinalJson = ltEvento1Json[1].split(",");
                            String[] lnEventoFinalJson = lnEvento1Json[1].split("\n");
                            ltEvento = Double.parseDouble(ltEventoFinalJson[0]);
                            lnEvento = Double.parseDouble(lnEventoFinalJson[0]);
                            LatLng direccionZoom = new LatLng(ltEvento, lnEvento);
                            float zoomLevel = 16.0f;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(direccionZoom, zoomLevel));
                            mMap.addMarker(new MarkerOptions().position(direccionZoom).title("Aquí es tu negocio"));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buscar_direccion:{
                if (direccion.getText()!=null){
                    ponerDireccionEnMapa(direccion.getText().toString());
                }
                break;
            }
        }
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
