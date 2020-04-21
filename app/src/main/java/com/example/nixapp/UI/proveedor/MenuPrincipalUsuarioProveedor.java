package com.example.nixapp.UI.proveedor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.ayuda.AyudaProveedor;
import com.example.nixapp.UI.proveedor.calendario.CalendarioProveedor;
import com.example.nixapp.UI.proveedor.misServicios.MisServiciosProveedor;
import com.example.nixapp.UI.proveedor.serviciosProximos.ServiciosProximosProveedor;
import com.google.android.material.navigation.NavigationView;

public class MenuPrincipalUsuarioProveedor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_usuario_proveedor);

        Toolbar toolbar = findViewById(R.id.toolbar_proveedor);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_usuario_proveedor);
        NavigationView navigationView = findViewById(R.id.nav_view_usuario_proveedor);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_misservicios_proveedor:{
                Intent intentServicios = new Intent (MenuPrincipalUsuarioProveedor.this, MisServiciosProveedor.class);
                startActivity(intentServicios);
                break;
            }
            case R.id.nav_calendario_proveedor:{
                Intent intentCalendario = new Intent (MenuPrincipalUsuarioProveedor.this, CalendarioProveedor.class);
                startActivity(intentCalendario);
                break;
            }
            case R.id.nav_ayuda_proveedor:{
                Intent intentAyuda = new Intent (MenuPrincipalUsuarioProveedor.this, AyudaProveedor.class);
                startActivity(intentAyuda);
                break;
            }
            case R.id.nav_serviciosproximos_proveedor:{
                Intent intentServiciosProx = new Intent (MenuPrincipalUsuarioProveedor.this, ServiciosProximosProveedor.class);
                startActivity(intentServiciosProx);
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
