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

public class MiPerfil extends AppCompatActivity {

    boolean estado = true;
    int seleccion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        BottomNavigationView bottomNav = findViewById(R.id.menu_abajo_mi_perfil);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Mi Perfil");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mi_perfil,new InformacionPersonalFragment()).commit();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /////////////////////////////////////////////////////

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
                        case R.id.nav_informacionpersonal:{
                            setToolbarTitle("Mi Perfil");
                            selectedFragment = new InformacionPersonalFragment();
                            seleccion = 0;
                            break;
                        }
                        case R.id.nav_metodospago:{
                            setToolbarTitle("Métodos de Pago");
                            selectedFragment = new MetodosPagoFragment();
                            seleccion = 1;
                            break;
                        }
                    }
                    if(seleccion == 0 && estado != true)
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.fragment_container_mi_perfil, selectedFragment).commit();
                        estado = true;
                    }
                    else if (seleccion == 1 && estado != false)
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).replace(R.id.fragment_container_mi_perfil, selectedFragment).commit();
                        estado = false;
                    }

                    return true;
                }
            };
}
