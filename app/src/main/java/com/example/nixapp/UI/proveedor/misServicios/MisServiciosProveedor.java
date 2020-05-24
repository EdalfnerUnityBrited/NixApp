package com.example.nixapp.UI.proveedor.misServicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisServiciosProveedor extends AppCompatActivity implements CrearServiciosFragmentProveedor.OnListFragmentInteractionListener {

    NixService nixService;
    NixClient nixClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_servicios_proveedor);
        retrofitInit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_abajo_mis_servicios_proveedor);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mis_servicios_proveedor,new CrearServiciosFragmentProveedor()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_crearservicios:{
                            selectedFragment = new CrearServiciosFragmentProveedor();
                            break;
                        }
                        case R.id.nav_historial_proveedor:{
                            selectedFragment = new HistorialFragmentProveedor();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mis_servicios_proveedor,selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public void onListFragmentInteraction(CatalogoServicios item) {
    }

    @Override
    public void onClickDelete(final CatalogoServicios item) {
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Quiere borrar el evento "+item.getNombre()+"?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                CatalogoServicios eventos=item;
                Call<ResponseBody> call = nixService.borrarServicio(item);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(MisServiciosProveedor.this, "Servicio borrado satisfactoriamente", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MisServiciosProveedor.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MisServiciosProveedor.this, "Error", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClickEdit(CatalogoServicios item) {
        Toast.makeText(this, "Editar apretado", Toast.LENGTH_SHORT).show();
        Intent intentCrearServicioMenu = new Intent(MisServiciosProveedor.this, CrearServicioMenu.class);
        intentCrearServicioMenu.putExtra("id",item.getId());
        startActivity(intentCrearServicioMenu);
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
