package com.example.nixapp.UI.proveedor.misServicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CrearServicioMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_servicio_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_abajo_crear_servicio);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_crear_servicio_menu_proveedor,new InformacionFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_infoservicio_menu:{
                            selectedFragment = new InformacionFragment();
                            break;
                        }
                        case R.id.nav_articulos:{
                            selectedFragment = new ArticuloFragment();
                            break;
                        }
                        case R.id.nav_paquetes:{
                            selectedFragment = new PaquetesFragment();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_crear_servicio_menu_proveedor,selectedFragment).commit();
                    return true;
                }
            };
}
