package com.example.nixapp.UI.proveedor.misServicios.Articulos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.ImagenArticulo;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ArticuloResult;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearArticulosDatos extends AppCompatActivity {

    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_INTENT=1;
    List<String> fotos;
    List<ImagenArticulo> imagenArticulos;
    String downloadUrl, imagenPrincipal="";
    int contador = 0;
    int ApiActivada = 0;
    Articulos arti;
    NixService nixService;
    NixClient nixClient;
    int cat;
    Spinner categorias,precio_por;
    String[] categorias_texto = new String[]{"Mobiliario","Sonido","Terrazas/Casinos/Terrenos", "Alimentos y Bebidas" , "Licores" , "Botanas", "Decoracion" , "Personal/Empleados", "Desechables", "Cristaleria"};
    String[] preciopor = new String[]{ "Unidad" ,"Hora","Persona"};
    EditText nombre, costo, descripcion;
    Button terminarServicio, agregarImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_articulo);
        imagenArticulos= new ArrayList<>();
        final int idArticulo= (int) getIntent().getSerializableExtra("id");
        mStorage= FirebaseStorage.getInstance().getReference().child("Fotos");
        mProgressDialog= new ProgressDialog(this);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        retrofitinit();
        downloadUrl="";
        imagenPrincipal="";
        fotos= new ArrayList<>();
        nombre= findViewById(R.id.nombre);
        costo=findViewById(R.id.costo);
        descripcion=findViewById(R.id.descripcion);
        terminarServicio=findViewById(R.id.servicioTerminado);
        agregarImagen=findViewById(R.id.buttonImagen);
        agregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        terminarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreArticulo= nombre.getText().toString();
                String costoArticulo= costo.getText().toString();
                String descripcionArticulo= descripcion.getText().toString();
                String precio= costo.getText().toString();
                String precioPorArticulo= precio_por.getSelectedItem().toString();
                mProgressDialog.setTitle("Creando articulo...");
                mProgressDialog.setMessage("Por favor espere");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Articulos articulos = new Articulos(nombreArticulo,descripcionArticulo,precioPorArticulo,precio,cat,idArticulo);
                Call<ArticuloResult> call= nixService.nuevoArticulo(articulos);
                call.enqueue(new Callback<ArticuloResult>() {
                    @Override
                    public void onResponse(Call<ArticuloResult> call, Response<ArticuloResult> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(CrearArticulosDatos.this, "Articulo añadido", Toast.LENGTH_SHORT).show();
                            arti= response.body().articulo;
                            if (!fotos.isEmpty()){
                                for (String imagenes: fotos){
                                    ImagenArticulo imagenEventos1 = new ImagenArticulo(imagenes,arti.getId());
                                    imagenArticulos.add(imagenEventos1);
                                }
                                Call<ResponseBody> callDos = nixService.imagenArticulo(imagenArticulos);
                                callDos.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                       if (response.isSuccessful()){
                                           Toast.makeText(CrearArticulosDatos.this, "Imagenes añadidas", Toast.LENGTH_SHORT).show();
                                       }
                                       else {
                                           Toast.makeText(CrearArticulosDatos.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                           Log.i("error", response.errorBody().toString());
                                       }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            mProgressDialog.dismiss();
                        }
                        else{
                            Toast.makeText(CrearArticulosDatos.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                            Log.i("error",response.errorBody().toString());
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArticuloResult> call, Throwable t) {

                    }
                });
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        categorias = findViewById(R.id.spinner_categorias);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.texto_municipios,categorias_texto
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
        categorias.setAdapter(spinnerArrayAdapter);
        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        precio_por = findViewById(R.id.spinner_precioPor);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                this,R.layout.texto_municipios,preciopor
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
        precio_por.setAdapter(spinnerArrayAdapter1);
        precio_por.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ApiActivada == 0) {
            super.onActivityResult(requestCode, resultCode, data);
            int length = fotos.size();
            if (length < 3) {
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
                            Toast.makeText(CrearArticulosDatos.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CrearArticulosDatos.this, "Subida Exitosa", Toast.LENGTH_SHORT).show();
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
                                    if (fotos.isEmpty()) {
                                        imagenPrincipal = downloadUrl;
                                    }
                                    fotos.add(downloadUrl);
                                    mProgressDialog.dismiss();
                                    ImagenEventos nueva = new ImagenEventos(downloadUrl);
                                    contador++;

                                }
                            });


                        }
                    });
                }
            }
            else {
                Toast.makeText(this, "Solo puede agregar 7 fotos", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    Toast.makeText(this, "Imagen Agregada", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
            ApiActivada = 0;
        }

    }

}
