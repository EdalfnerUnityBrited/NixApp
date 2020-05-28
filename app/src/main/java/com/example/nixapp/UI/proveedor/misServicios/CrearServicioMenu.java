package com.example.nixapp.UI.proveedor.misServicios;

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

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.misServicios.Articulos.ArticuloFragment;
import com.example.nixapp.UI.proveedor.misServicios.Articulos.EditarArticulosDatos;
import com.example.nixapp.UI.proveedor.misServicios.Paquetes.PaquetesFragment;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearServicioMenu extends AppCompatActivity implements PaquetesFragment.OnListFragmentInteractionListener, ArticuloFragment.OnListFragmentInteractionListener {
    public static int servicio= 0;
    NixService nixService;
    NixClient nixClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_servicio_menu);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Informacion del servicio");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_abajo_crear_servicio);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        servicio = (int) getIntent().getSerializableExtra("id");
        retrofitInit();
        Toast.makeText(this, "Servicio"+Integer.toString(servicio), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_crear_servicio_menu_proveedor,new InformacionFragment()).commit();
    }
    public void setToolbarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_infoservicio_menu:{
                            setToolbarTitle("Informacion del servicio");
                            selectedFragment = new InformacionFragment();
                            break;
                        }
                        case R.id.nav_articulos:{
                            setToolbarTitle("Añadir articulos");
                            selectedFragment = new ArticuloFragment();
                            break;
                        }
                        case R.id.nav_paquetes:{
                            setToolbarTitle("Añadir paquetes");
                            selectedFragment = new PaquetesFragment();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_crear_servicio_menu_proveedor,selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public void onListFragmentInteraction(Paquetes item) {
        Toast.makeText(this, "Apretaste el paquete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickDelete(final Paquetes item) {
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Quiere borrar el paquete "+item.getNombre()+"?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Paquetes eventos=item;
                Call<ResponseBody> call = nixService.borrarPaquete(item);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(CrearServicioMenu.this, "Paquete borrado satisfactoriamente", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(CrearServicioMenu.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(CrearServicioMenu.this, "Error", Toast.LENGTH_SHORT).show();
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
    public void onListFragmentInteraction(Articulos item) {
        Toast.makeText(this, "Apretaste el articulo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickDelete(final Articulos item) {
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Quiere borrar el articulo "+item.getNombre()+"?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Articulos eventos=item;
                Call<ResponseBody> call = nixService.borrarArticulo(item);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(CrearServicioMenu.this, "Articulo borrado satisfactoriamente", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(CrearServicioMenu.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(CrearServicioMenu.this, "Error", Toast.LENGTH_SHORT).show();
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
    public void onClickEdit(Articulos item) {
        Intent intentArticulo = new Intent(CrearServicioMenu.this, EditarArticulosDatos.class);
        intentArticulo.putExtra("id",item.getId());
        startActivity(intentArticulo);
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
