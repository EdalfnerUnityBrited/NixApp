package com.example.nixapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nixapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MenuPrincipalUsuarioGeneral extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    Double EventLatitude, EventLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_usuario_general);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout_usuario_general);
        NavigationView navigationView = findViewById(R.id.nav_view_usuario_general);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_miseventos:{
                Intent intentMisEventos = new Intent(this,MisEventos.class);
                startActivity(intentMisEventos);
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void setActionBar(Toolbar toolbar) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION},MY_PERMISSION_FINE_LOCATION);
            }
        }

        // Add a marker in Sydney and move the camera
        LatLng GDL = new LatLng(20,-103);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(GDL));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_FINE_LOCATION:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Nix necesita conocer tu ubicación",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }
        }
    }
}
