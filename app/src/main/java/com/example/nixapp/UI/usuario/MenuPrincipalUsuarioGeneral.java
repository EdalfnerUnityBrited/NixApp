package com.example.nixapp.UI.usuario;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.DB.controllers.TokenController;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.configUsuario.MiPerfil;
import com.example.nixapp.UI.usuario.misEventos.MisEventos;
import com.example.nixapp.UI.usuario.ayuda.Ayuda;
import com.example.nixapp.UI.usuario.eventosProximos.EventosProximos;
import com.example.nixapp.UI.usuario.serviciosContratados.ServiciosProximos;
import com.example.nixapp.UI.welcome.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MenuPrincipalUsuarioGeneral extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    Double EventLatitude, EventLongitude;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // Add a marker in Sydney and move the camera
        LatLng GDL = new LatLng(20, -103);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(GDL));
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
}
