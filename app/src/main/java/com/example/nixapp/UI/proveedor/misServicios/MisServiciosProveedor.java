package com.example.nixapp.UI.proveedor.misServicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MisServiciosProveedor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_servicios_proveedor);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_abajo_mis_servicios_proveedor);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mis_servicios_proveedor,new CrearServiciosFragmentProveedor()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_crearservicios:{
                            selectedFragment = new CrearServiciosFragmentProveedor();
                            break;
                        }
                        case R.id.nav_historial_proveedor:{
                            selectedFragment = new HistorialFragmentProveedor();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mis_servicios_proveedor,selectedFragment).commit();
                    return true;
                }
            };
}
