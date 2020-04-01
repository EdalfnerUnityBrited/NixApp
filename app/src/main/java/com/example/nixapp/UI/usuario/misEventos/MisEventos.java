package com.example.nixapp.UI.usuario.misEventos;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MisEventos extends AppCompatActivity implements EventosCerradosFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);

        BottomNavigationView bottomNav = findViewById(R.id.menu_abajo_mis_eventos);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Mis eventos");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mis_eventos,new EventosCerradosFragment()).commit();

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
                        case R.id.nav_eventoscerrados:{
                            setToolbarTitle("Mis eventos");
                            selectedFragment = new EventosCerradosFragment();

                            break;
                        }
                        case R.id.nav_historial:{
                            setToolbarTitle("Historial de eventos");
                            selectedFragment = new HistorialFragment();

                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mis_eventos, selectedFragment).commit();
                    return true;
                }
            };

    public void onListFragmentInteraction(Eventos item) {


        //Intent chatSelected= new Intent(getBaseContext(), ChatActivity.class);


        //startActivity(chatSelected);

    }

}
