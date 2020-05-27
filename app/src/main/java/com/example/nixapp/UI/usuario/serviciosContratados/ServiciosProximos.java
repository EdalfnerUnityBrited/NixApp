package com.example.nixapp.UI.usuario.serviciosContratados;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Chat;
import com.example.nixapp.DB.Cotizacion;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico.CotizacionServicio;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.ChatActivity;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.DashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ServiciosProximos extends AppCompatActivity implements DashboardFragment.OnListFragmentInteractionListener, CotizacionesGuardadasFragment.OnListFragmentInteractionListener{
    private RecyclerView chatsList;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        setContentView(R.layout.activity_servicios_proximos);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_serviciosproximos_menu, R.id.nav_cotizacionesguardadas, R.id.nav_chats)
                .build();
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_serviciosproximos_menu:
                    {
                        mToolbar.setTitle("Servicios Proximos");
                        setFragment(new ServiciosProximosFragment());
                        return true;
                    }
                    case R.id.nav_cotizacionesguardadas:
                    {
                        mToolbar.setTitle("Mis Cotizaciones");
                        setFragment(new CotizacionesGuardadasFragment());
                        return true;
                    }
                    case  R.id.nav_chats:{

                        mToolbar.setTitle("Mis Chats");
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

    @Override
    public void onListFragmentInteraction(Cotizacion item) {
        Toast.makeText(this, "Apretaste la cotización: "+item.getId(), Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(ServiciosProximos.this, CotizacionServicio.class);
        intent.putExtra("id", item.getId_servicio());
        intent.putExtra("id_cotizacion", item.getId());
        intent.putExtra("id_Evento", item.getId_evento());//int
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
