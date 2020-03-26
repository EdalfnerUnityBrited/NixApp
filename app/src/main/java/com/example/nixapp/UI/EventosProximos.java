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

public class EventosProximos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_proximos);

        BottomNavigationView bottomNav = findViewById(R.id.menu_abajo_eventos_proximos);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Eventos Próximos");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_eventos_proximos,new EventosProximosFragment()).commit();

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
                        case R.id.nav_eventosproximos_menu:{
                            setToolbarTitle("Eventos Próximos");
                            selectedFragment = new EventosProximosFragment();
                            break;
                        }
                        case R.id.nav_misnotificaciones:{
                            setToolbarTitle("Mis Notificaciones");
                            selectedFragment = new MisNotificacionesFragment();
                            break;
                        }
                        case R.id.nav_misintereses:{
                            setToolbarTitle("Mis Intereses");
                            selectedFragment = new MisInteresesFragment();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_eventos_proximos, selectedFragment).commit();
                    return true;
                }
            };
}
