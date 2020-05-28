package com.example.nixapp.UI.proveedor.ayuda;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.nixapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AyudaProveedor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_proveedor);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Menu de Ayuda");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_abajo_ayudaproveedor);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ayudaproveedor,new MenuAyudaProveedorFragment()).commit();
    }
    public void setToolbarTitle(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_menudeayuda_proveedor:{
                            setToolbarTitle("Menu de Ayuda");
                            selectedFragment = new MenuAyudaProveedorFragment();
                            break;
                        }
                        case R.id.nav_tutoriales_proveedor:{
                            setToolbarTitle("Video-Tutoriales");
                            selectedFragment = new TutorialesProveedorFragment();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ayudaproveedor,selectedFragment).commit();
                    return true;
                }
            };
}
