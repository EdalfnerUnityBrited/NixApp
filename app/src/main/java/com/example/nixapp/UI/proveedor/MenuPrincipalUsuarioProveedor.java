package com.example.nixapp.UI.proveedor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.DB.controllers.TokenController;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.ayuda.AyudaProveedor;
import com.example.nixapp.UI.proveedor.calendario.CalendarioProveedor;
import com.example.nixapp.UI.proveedor.misServicios.MisServiciosProveedor;
import com.example.nixapp.UI.proveedor.serviciosProximos.ServiciosProximosProveedor;
import com.example.nixapp.UI.welcome.MainActivity;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuPrincipalUsuarioProveedor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    TextView nombre, email;
    CircleImageView fotoPerfil;
    Usuario usuario;
    String downloadUrl;
    NixService nixService;
    NixClient nixClient;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_INTENT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_usuario_proveedor);

        Toolbar toolbar = findViewById(R.id.toolbar_proveedor);
        setSupportActionBar(toolbar);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        mStorage= FirebaseStorage.getInstance().getReference().child("Fotos");
        mProgressDialog= new ProgressDialog(MenuPrincipalUsuarioProveedor.this);
        drawerLayout = findViewById(R.id.drawer_layout_usuario_proveedor);
        NavigationView navigationView = findViewById(R.id.nav_view_usuario_proveedor);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        retrofitInit();
        View hView = navigationView.getHeaderView(0);
        nombre= hView.findViewById(R.id.tv_nombre_proveedor);
        email= hView.findViewById(R.id.tv_email_proveedor);
        fotoPerfil= hView.findViewById(R.id.profile_image_proveedor);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        nombre.setText(usuario.name);
        email.setText(usuario.email);
        Glide.with(fotoPerfil)
                .load(usuario.fotoPerfil)
                .into(fotoPerfil);
        toggle.syncState();
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_misservicios_proveedor:{
                Intent intentServicios = new Intent (MenuPrincipalUsuarioProveedor.this, MisServiciosProveedor.class);
                startActivity(intentServicios);
                break;
            }
            case R.id.nav_calendario_proveedor:{
                Intent intentCalendario = new Intent (MenuPrincipalUsuarioProveedor.this, CalendarioProveedor.class);
                startActivity(intentCalendario);
                break;
            }
            case R.id.nav_ayuda_proveedor:{
                Intent intentAyuda = new Intent (MenuPrincipalUsuarioProveedor.this, AyudaProveedor.class);
                startActivity(intentAyuda);
                break;
            }
            case R.id.nav_serviciosproximos_proveedor:{
                Intent intentServiciosProx = new Intent (MenuPrincipalUsuarioProveedor.this, ServiciosProximosProveedor.class);
                intentServiciosProx.putExtra("usuario", usuario);
                startActivity(intentServiciosProx);
                break;
            }
            case R.id.nav_salir:{
                TokenController.getToken().delete();
                Intent intentVuelta = new Intent(this, MainActivity.class);
                startActivity(intentVuelta);
                finish();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == -1) {
            mProgressDialog.setTitle("Subiendo...");
            mProgressDialog.setMessage("Subiendo foto a firebase");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            Uri uri = data.getData();
            final StorageReference filePath = mStorage.child(uri.getLastPathSegment());
            final UploadTask uploadTask = filePath.putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(MenuPrincipalUsuarioProveedor.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MenuPrincipalUsuarioProveedor.this, "Subida Exitosa", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadUrl = task.getResult().toString();
                            mProgressDialog.dismiss();
                            Glide.with(fotoPerfil)
                                    .load(downloadUrl)
                                    .into(fotoPerfil);
                            final Usuario requestSample = new Usuario(downloadUrl);
                            Call<ResponseBody> call = nixService.foto(requestSample);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(MenuPrincipalUsuarioProveedor.this, "Cambio de foto correcto",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MenuPrincipalUsuarioProveedor.this, "Error en los datos",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                        }
                    });


                }
            });
        }

    }
    }

