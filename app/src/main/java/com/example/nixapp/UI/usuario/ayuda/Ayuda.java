package com.example.nixapp.UI.usuario.ayuda;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Ayuda extends AppCompatActivity {

    int fragment = 0;
    int presionado = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        BottomNavigationView bottomNav = findViewById(R.id.menu_abajo_ayuda);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Ayuda");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ayuda,new MenuAyudaFragment()).commit();

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
                        case R.id.nav_menudeayuda:{
                            setToolbarTitle("Ayuda");
                            selectedFragment = new MenuAyudaFragment();
                            fragment = 1;
                            break;
                        }
                        case R.id.nav_tutoriales:{
                            setToolbarTitle("Tutoriales");
                            selectedFragment = new TutorialesFragment();
                            fragment = 2;
                            break;
                        }
                    }
                    if(fragment == 1 && presionado == 1)
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.fragment_container_ayuda, selectedFragment).commit();
                        presionado = 2;
                    }
                    else if(fragment == 2 && presionado == 2)
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).replace(R.id.fragment_container_ayuda, selectedFragment).commit();
                        presionado = 1;
                    }
                    return true;
                }
            };
}
