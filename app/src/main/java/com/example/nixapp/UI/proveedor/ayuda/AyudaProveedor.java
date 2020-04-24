package com.example.nixapp.UI.proveedor.ayuda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AyudaProveedor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_proveedor);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_abajo_ayudaproveedor);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ayudaproveedor,new MenuAyudaProveedorFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_menudeayuda_proveedor:{
                            selectedFragment = new MenuAyudaProveedorFragment();
                            break;
                        }
                        case R.id.nav_tutoriales_proveedor:{
                            selectedFragment = new TutorialesProveedorFragment();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ayudaproveedor,selectedFragment).commit();
                    return true;
                }
            };
}
