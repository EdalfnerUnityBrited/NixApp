package com.example.nixapp.UI.usuario;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.DB.controllers.TokenController;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.Calendario.Calendario;
import com.example.nixapp.UI.usuario.Interfaces.InfoEventoFragmentListener;
import com.example.nixapp.UI.usuario.ayuda.Ayuda;
import com.example.nixapp.UI.usuario.configUsuario.MiPerfil;
import com.example.nixapp.UI.usuario.eventosProximos.EventosProximos;
import com.example.nixapp.UI.usuario.misEventos.MisEventos;
import com.example.nixapp.UI.usuario.serviciosContratados.ServiciosProximos;
import com.example.nixapp.UI.welcome.MainActivity;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosTodosResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MenuPrincipalUsuarioGeneral extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, InfoEventoFragmentListener {
    private DrawerLayout drawer;
    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    Double EventLatitude, EventLongitude;
    Usuario usuario;
    private NixService nixService;
    private NixClient nixClient;
    Button buscar;

    private List <String> infoCompletaEventoEspecifico = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitinit();
        setContentView(R.layout.activity_menu_principal_usuario_general);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout_usuario_general);
        NavigationView navigationView = findViewById(R.id.nav_view_usuario_general);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        TextView tv_nombre = hView.findViewById(R.id.tv_nombre);
        TextView tv_email = hView.findViewById(R.id.tv_email);
        CircleImageView profile=hView.findViewById(R.id.profile_image);
        Glide.with(profile)
                .load(usuario.fotoPerfil)
                .into(profile);
        tv_nombre.setText(usuario.name);
        tv_email.setText(usuario.email);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buscar = findViewById(R.id.buscarEventos);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBusqueda = new Intent(getApplicationContext(), BuscarEventos.class);
                intentBusqueda.putExtra("usuario", usuario);
                startActivity(intentBusqueda);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(false);
        switch (menuItem.getItemId()) {
            case R.id.nav_miseventos: {
                Intent intentMisEventos = new Intent(this, MisEventos.class);
                startActivity(intentMisEventos);
                break;
            }
            case R.id.nav_serviciosproximos: {
                Intent intentServiciosProximos = new Intent(this, ServiciosProximos.class);
                intentServiciosProximos.putExtra("usuario", usuario);
                startActivity(intentServiciosProximos);
                break;
            }
            case R.id.nav_eventosproximos: {
                Intent intentEventosProximos = new Intent(this, EventosProximos.class);
                startActivity(intentEventosProximos);
                break;
            }
            case R.id.nav_salir:{
                TokenController.getToken().delete();
                Intent intentVuelta = new Intent(this, MainActivity.class);
                startActivity(intentVuelta);
                finish();
                break;
            }
            case R.id.nav_perfil:{
                Intent intentMiPerfil = new Intent(this, MiPerfil.class);
                intentMiPerfil.putExtra("usuario", usuario);
                startActivity(intentMiPerfil);
                break;
            }
            case R.id.nav_ayuda:{
                Intent intentAyuda = new Intent(this, Ayuda.class);
                startActivity(intentAyuda);
                break;
            }
            case R.id.nav_calendario:{
                Intent intentCalendario = new Intent(this, Calendario.class);
                startActivity(intentCalendario);
                break;
            }
            case R.id.nav_tendencia:{
                Intent intentTendencia = new Intent(this, Tendencias.class);
                startActivity(intentTendencia);
                break;
            }
        }

        menuItem.setChecked(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setActionBar(Toolbar toolbar) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
        //hacer zoom al usuario
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        // agregar todos los eventos al mapa
        Call<EventosTodosResult> call = nixService.eventosTodos();
        call.enqueue(new Callback<EventosTodosResult>() {
            @Override
            public void onResponse(Call<EventosTodosResult> call, Response<EventosTodosResult> response) {
                if (response.isSuccessful()) {
                    for (final Eventos x: response.body().eventos) {
                        String direccion = x.getLugar();
                        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?&address=%1$s+Jalisco+Mexico&key=AIzaSyAPGGYxsJfpi3DY0o11lAR4-Gccfpf3juw",direccion);
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        client.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {
                                Toast.makeText(MenuPrincipalUsuarioGeneral.this, "ERROR API GEOCODING", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    final String myResponse = response.body().string();
                                    MenuPrincipalUsuarioGeneral.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                           double ltEvento, lnEvento;
                                            String[] ltEvento1Json = myResponse.split("\"lat\"+ +:+ ");
                                            String[] lnEvento1Json = myResponse.split("\"lng\"+ +:+ ");
                                            String[] ltEventoFinalJson = ltEvento1Json[1].split(",");
                                            String[] lnEventoFinalJson = lnEvento1Json[1].split("\n");
                                            ltEvento = Double.parseDouble(ltEventoFinalJson[0]);
                                            lnEvento = Double.parseDouble(lnEventoFinalJson[0]);
                                            LatLng evento = new LatLng(ltEvento, lnEvento);
                                            mMap.addMarker(new MarkerOptions().position(evento).title(x.getNombre_evento()));
                                        }
                                    });
                                }
                            }
                        });
                    }
                    Toast.makeText(MenuPrincipalUsuarioGeneral.this, "AGREGADOS LOS EVENTOS AL MAPA", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MenuPrincipalUsuarioGeneral.this, "ERROR AGREGAR EVENTOS AL MAPA", Toast.LENGTH_SHORT).show();
                    try {
                        Log.i("Error",response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<EventosTodosResult> call, Throwable t) {

            }
        });
        mMap.setOnMarkerClickListener(this);
    }

    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Nix necesita conocer tu ubicación", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Eventos event =new Eventos(marker.getTitle());
        Call<JsonObject> calle = nixService.eventoBuscar(event);
        calle.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, final Response<JsonObject> response) {
                if (response.isSuccessful()){
                    MenuPrincipalUsuarioGeneral.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle data = new Bundle();
                            String[] ayuda = String.valueOf(response.body()).split("\"fecha\"+:+\"+");//1
                            String[] ayudaFinal = ayuda[1].split("\"");
                            data.putString("fecha",ayudaFinal[0]);
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);
                            ayuda = String.valueOf(response.body()).split("\"hora\"+:+\"+");//2
                            ayudaFinal = ayuda[1].split("\"");
                            data.putString("hora",ayudaFinal[0]);
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);
                            ayuda = String.valueOf(response.body()).split("\"nombre_evento\"+:+\"+");//3
                            ayudaFinal = ayuda[1].split("\"");
                            data.putString("nombre",ayudaFinal[0]);
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);
                            ayuda = String.valueOf(response.body()).split("\"lugar\"+:+\"+");//4
                            ayudaFinal = ayuda[1].split("\"");
                            data.putString("lugar",ayudaFinal[0]);
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);

                            ayuda = String.valueOf(response.body()).split("\"privacidad\"+:");//5
                            ayudaFinal = ayuda[1].split(",");
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);
                            ayuda = String.valueOf(response.body()).split("\"categoria_evento\"+:");//6
                            ayudaFinal = ayuda[1].split(",");
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);
                            ayuda = String.valueOf(response.body()).split("\"descripcion\"+:+\"+");//7
                            ayudaFinal = ayuda[1].split("\"");
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);
                            ayuda = String.valueOf(response.body()).split("\"cupo\"+:");//8
                            ayudaFinal = ayuda[1].split(",");
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);
                            ayuda = String.valueOf(response.body()).split("\"cover\"+:");//9
                            ayudaFinal = ayuda[1].split(",");
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);
                            ayuda = String.valueOf(response.body()).split("\"fotoPrincipal\"+:+\"+");//10
                            ayudaFinal = ayuda[1].split("\"");
                            infoCompletaEventoEspecifico.add(ayudaFinal[0]);

                            InfoEventoFragment infoEventoFragment = InfoEventoFragment.getInstance();
                            infoEventoFragment.setListener(MenuPrincipalUsuarioGeneral.this);
                            infoEventoFragment.setArguments(data);
                            infoEventoFragment.show(getSupportFragmentManager(),infoEventoFragment.getTag());
                        }
                    });

                }
                else{
                    Toast.makeText(MenuPrincipalUsuarioGeneral.this, "Fallo en Response", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MenuPrincipalUsuarioGeneral.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    @Override
    public void onMoreInfoClickListener() {
        Intent intentInfoExpandida = new Intent(MenuPrincipalUsuarioGeneral.this,InfoEventoExpandida.class);
        intentInfoExpandida.putExtra("nombre",infoCompletaEventoEspecifico.get(2));
        intentInfoExpandida.putExtra("privacidad",infoCompletaEventoEspecifico.get(4));
        intentInfoExpandida.putExtra("categoria",infoCompletaEventoEspecifico.get(5));
        intentInfoExpandida.putExtra("fecha",infoCompletaEventoEspecifico.get(0));
        intentInfoExpandida.putExtra("hora",infoCompletaEventoEspecifico.get(1));
        intentInfoExpandida.putExtra("lugar",infoCompletaEventoEspecifico.get(3));
        intentInfoExpandida.putExtra("descripcion",infoCompletaEventoEspecifico.get(6));
        intentInfoExpandida.putExtra("cupo",infoCompletaEventoEspecifico.get(7));
        intentInfoExpandida.putExtra("cover",infoCompletaEventoEspecifico.get(8));
        intentInfoExpandida.putExtra("fotoPrincipal",infoCompletaEventoEspecifico.get(9));
        startActivity(intentInfoExpandida);
    }
}
