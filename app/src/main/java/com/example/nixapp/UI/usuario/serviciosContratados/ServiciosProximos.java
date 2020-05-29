package com.example.nixapp.UI.usuario.serviciosContratados;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Chat;
import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.DB.Cotizacion;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.InfoExpandidaServicio;
import com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico.CotizacionServicio;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.ChatActivity;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.DashboardFragment;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiciosProximos extends AppCompatActivity implements DashboardFragment.OnListFragmentInteractionListener, CotizacionesGuardadasFragment.OnListFragmentInteractionListener,ServiciosProximosFragment.OnListFragmentInteractionListener{
    private RecyclerView chatsList;
    Usuario usuario;
    NixService nixService;
    NixClient nixClient;
    int fragment_origen = 1;
    int fragment_destino = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        setContentView(R.layout.activity_servicios_proximos);
        retrofitInit();
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
                        fragment_destino = 1;
                        setFragment(new ServiciosProximosFragment());
                        return true;
                    }
                    case R.id.nav_cotizacionesguardadas:
                    {
                        mToolbar.setTitle("Mis Cotizaciones");
                        fragment_destino = 2;
                        setFragment(new CotizacionesGuardadasFragment());
                        return true;
                    }
                    case  R.id.nav_chats:{

                        mToolbar.setTitle("Mis Chats");
                        fragment_destino = 3;
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

        if(fragment_destino == 1 && (fragment_origen == 2||fragment_origen == 3))
        {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.framLayout, fragment).commit();
            fragment_origen = 1;
        }
        else if(fragment_destino == 2 && fragment_origen == 1)
        {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).replace(R.id.framLayout, fragment).commit();
            fragment_origen = 2;
        }
        else if(fragment_destino == 2 && fragment_origen == 3)
        {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.framLayout, fragment).commit();
            fragment_origen = 2;
        }
        else if(fragment_destino == 3 && (fragment_origen == 2||fragment_origen == 1))
        {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).replace(R.id.framLayout, fragment).commit();
            fragment_origen = 3;
        }

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
        Intent intent= new Intent(ServiciosProximos.this, CotizacionServicio.class);
        intent.putExtra("id", item.getId_servicio());
        intent.putExtra("id_cotizacion", item.getId());
        intent.putExtra("id_Evento", item.getId_evento());//int
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Contrataciones item) {
        Intent intent= new Intent(ServiciosProximos.this, InfoExpandidaServicio.class);
        intent.putExtra("id_contratacion", item.getId());
        intent.putExtra("ingreso",2);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClickDelete(final Cotizacion mItem) {
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Quiere borrar la cotizacion del servicio: "+mItem.getNombre()+"?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Call<ResponseBody> call = nixService.borrarCotizacion(mItem);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(ServiciosProximos.this, "Cotizacion borrada satisfactoriamente", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ServiciosProximos.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ServiciosProximos.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //cancelar();
            }
        });
        dialogo1.show();
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
