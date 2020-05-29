package com.example.nixapp.UI.usuario;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.DB.controllers.TokenController;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.BusquedaEventos.BuscarEventos;
import com.example.nixapp.UI.usuario.Calendario.Calendario;
import com.example.nixapp.UI.usuario.Interfaces.InfoEventoFragmentListener;
import com.example.nixapp.UI.usuario.Tendencias.Tendencias;
import com.example.nixapp.UI.usuario.ayuda.Ayuda;
import com.example.nixapp.UI.usuario.configUsuario.MiPerfil;
import com.example.nixapp.UI.usuario.eventosProximos.EventosProximos;
import com.example.nixapp.UI.usuario.misEventos.MisEventos;
import com.example.nixapp.UI.usuario.serviciosContratados.ServiciosProximos;
import com.example.nixapp.UI.welcome.MainActivity;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosResult;
import com.example.nixapp.conn.results.EventosTodosResult;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

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
    private long mLastClickTime = 0;
    GoogleSignInClient mGoogleSignInClient;
    Button buscar;
    Eventos eventos;
    AlertDialog.Builder dialogo1;

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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000){
                    buscar.setEnabled(false);
                    Intent intentBusqueda = new Intent(getApplicationContext(), BuscarEventos.class);
                    intentBusqueda.putExtra("usuario", usuario);
                    startActivity(intentBusqueda);
                    buscar.setEnabled(true);
                }
                mLastClickTime = SystemClock.elapsedRealtime();

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(false);
        if (SystemClock.elapsedRealtime() - mLastClickTime > 1000){
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
                    dialogo1 = new AlertDialog.Builder(this);
                    dialogo1.setTitle("Importante");
                    dialogo1.setMessage("¿ Quieres salir de la sesion iniciada ?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            TokenController.getToken().delete();
                            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            if(AccessToken.getCurrentAccessToken()!=null)
                            {
                                LoginManager.getInstance().logOut();
                            }
                            if(account != null)
                            {
                                signOut();
                            }
                            Intent intentVuelta = new Intent(MenuPrincipalUsuarioGeneral.this, MainActivity.class);
                            startActivity(intentVuelta);
                            finish();
                        }
                    });
                    dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //cancelar();
                        }
                    });
                    dialogo1.show();
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
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        menuItem.setChecked(false);
        return true;
    }

    private void signOut() {
         mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        //Toast.makeText(getApplicationContext(),"Saliste de la sesion" ,Toast.LENGTH_LONG).show();
                        // [END_EXCLUDE]
                    }
                });
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
                                            mMap.addMarker(new MarkerOptions().position(evento).title(x.getNombre_evento()).snippet(x.getId()));
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
        if (SystemClock.elapsedRealtime() - mLastClickTime > 1000){
            Busqueda event =new Busqueda(marker.getSnippet());
            Call<EventosResult> calle = nixService.buscarEventoId(event);
            calle.enqueue(new Callback<EventosResult>() {
                @Override
                public void onResponse(Call<EventosResult> call, final Response<EventosResult> response) {
                    if (response.isSuccessful()){
                        eventos= response.body().eventos;
                        MenuPrincipalUsuarioGeneral.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bundle data = new Bundle();

                                data.putString("fecha",eventos.getFecha());

                                data.putString("hora",eventos.getHora());

                                data.putString("nombre",eventos.getNombre_evento());

                                data.putString("lugar",eventos.getLugar());


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
                public void onFailure(Call<EventosResult> call, Throwable t) {
                    Toast.makeText(MenuPrincipalUsuarioGeneral.this,"Error en el intento: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

    @Override
    public void onMoreInfoClickListener() {
        Intent intentInfoExpandida = new Intent(MenuPrincipalUsuarioGeneral.this,InfoEventoExpandida.class);
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
}
