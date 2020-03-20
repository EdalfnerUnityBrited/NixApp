package com.example.nixapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MisEventos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);

        BottomNavigationView bottomNav = findViewById(R.id.menu_abajo_mis_eventos);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mis_eventos,new EventosCerradosFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_eventoscerrados:{
                            selectedFragment = new EventosCerradosFragment();
                            break;
                        }
                        case R.id.nav_historial:{
                            selectedFragment = new HistorialFragment();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mis_eventos, selectedFragment).commit();
                    return true;
                }
            };
}
