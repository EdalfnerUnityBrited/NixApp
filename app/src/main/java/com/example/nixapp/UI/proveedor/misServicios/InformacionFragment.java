package com.example.nixapp.UI.proveedor.misServicios;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.MenuPrincipalUsuarioGeneral;
import com.example.nixapp.UI.usuario.misEventos.EventosAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class InformacionFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    Spinner spinner , spinners;
    String[] Minicipios = new String[]{
            "Elige un municipio", "Acatic", "Ameca", "Arandas", "Atotonilco el alto", "Chapala", "Cocula", "El Arenal", "El Salto", "Guachinango", "Guadalajara",
            "Jocotepec", "La Barca", "Lagos de Moreno", "Mascota", "Mazamitla", "Mezquitic", "Puerto Vallarta", "San Juan de los Lagos", "Tlaquepaque", "Sayula",
            "Tala", "Tapalpa", "Tequila", "Tlajomulco de Zuñiga", "Tonala", "Tototlan", "Zapopan", "Zapotlanejo"
    };
    String municipio = "", clickedName;
    int categoria_evento;
    EditText hora_inicio,hora_fin,direccion;
    TextView municipios;
    TimePickerDialog hora_inicial,hora_final;
    Button agregar_mun,buscar_direccion;
    private EventosAdapter mAdapter;
    private ArrayList<EventosItems> mEventsList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infoservicio_proveedor,container,false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_servicio);
        mapFragment.getMapAsync(this);
        spinner = view.findViewById(R.id.spinner1);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.texto_municipios,Minicipios
        );
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

        mAdapter = new EventosAdapter(getActivity(), mEventsList);
        spinners.setAdapter(mAdapter);

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
                                hora_fin.setText(sHour + ":" + sMinute);
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
}
