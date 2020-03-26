package com.example.nixapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ServiciosProximos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_proximos);

        BottomNavigationView bottomNav = findViewById(R.id.menu_abajo_servicios_proximos);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Servicios Próximos");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_servicios_proximos,new ServiciosProximosFragment()).commit();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void setToolbarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_serviciosproximos_menu:{
                            setToolbarTitle("Servicios Próximos");
                            selectedFragment = new ServiciosProximosFragment();

                            break;
                        }
                        case R.id.nav_cotizacionesguardadas:{
                            setToolbarTitle("Cotizaciones Guardadas");
                            selectedFragment = new CotizacionesGuardadasFragment();

                            break;
                        }
                        case R.id.nav_chats:{
                            setToolbarTitle("Chats");
                            selectedFragment = new ChatsFragment();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_servicios_proximos, selectedFragment).commit();
                    return true;
                }
            };
}
