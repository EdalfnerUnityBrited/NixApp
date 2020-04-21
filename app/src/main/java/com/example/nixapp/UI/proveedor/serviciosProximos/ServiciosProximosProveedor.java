package com.example.nixapp.UI.proveedor.serviciosProximos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ServiciosProximosProveedor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_proximos_proveedor);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_abajo_servicios_proximos_proveedor);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_servicios_proximos_proveedor,new ServiciosPendientesFragmentProveedor()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_serviciospendientes:{
                            selectedFragment = new ServiciosPendientesFragmentProveedor();
                            break;
                        }
                        case R.id.nav_notificaciones_proveedor:{
                            selectedFragment = new NotificacionesFragmentProveedor();
                            break;
                        }
                        case R.id.nav_chats_proveedor:{
                            selectedFragment = new ChatsFragmentProveedor();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_servicios_proximos_proveedor,selectedFragment).commit();
                    return true;
                }
            };
}
