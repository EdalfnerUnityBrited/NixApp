package com.example.nixapp.UI.usuario.serviciosContratados;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Chat;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.ChatActivity;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.DashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ServiciosProximos extends AppCompatActivity implements DashboardFragment.OnListFragmentInteractionListener{
    private RecyclerView chatsList;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        setContentView(R.layout.activity_servicios_proximos);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_serviciosproximos, R.id.nav_cotizacionesguardadas, R.id.nav_chats)
                .build();
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_serviciosproximos:
                    {
                        setFragment(new ServiciosProximosFragment());
                        return true;
                    }
                    case R.id.nav_cotizacionesguardadas:
                    {
                        setFragment(new CotizacionesGuardadasFragment());
                        return true;
                    }
                    case  R.id.nav_chats:{

                        setFragment(new DashboardFragment());
                        return true;
                    }
                    default:
                        return false;
                }


            }
        });

        // chatsList=findViewById(R.id.nav_host_fragment);
        //chatsList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framLayout, fragment);
        fragmentTransaction.commit();
    }



    @Override
    public void onListFragmentInteraction(Chat item) {
        String idProveedor= String.valueOf(item.getId());

        Intent chatSelected= new Intent(getBaseContext(), ChatActivity.class);
        chatSelected.putExtra("sender", usuario.id);
        chatSelected.putExtra("reciver", idProveedor);
        startActivity(chatSelected);

    }

}
