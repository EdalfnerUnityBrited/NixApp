package com.example.nixapp.UI.proveedor.misServicios.Paquetes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.DB.ImagenPaquete;
import com.example.nixapp.DB.PaqueteArticulo;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ArticulosListResult;
import com.example.nixapp.conn.results.PaqueteResult;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
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

public class CrearPaquetesDatos extends AppCompatActivity {

    String[] itemsArray;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_INTENT=1;
    List<String> fotos, arts;
    String downloadUrl, imagenPrincipal="";
    int contador = 0;
    int ApiActivada = 0;
    Articulos arti;
    NixService nixService;
    NixClient nixClient;
    TableLayout stk;
    Button articulos_add, terminado, image;
    Spinner spinner;
    EditText cantidad, nombre, precio, descripcion;
    List<String> articulos;
    Paquetes paque;
    List<ImagenPaquete> imagenPaquete;
    List<PaqueteArticulo> paqueteArticulo;
    List<Articulos> articulosList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paquetes_datos);
        retrofitinit();
        image=findViewById(R.id.buttonImagen);
        imagenPaquete= new ArrayList<>();
        paqueteArticulo= new ArrayList<>();
        articulosList= new ArrayList<>();
        arts= new ArrayList<>();
        final int idArticulo= (int) getIntent().getSerializableExtra("id");
        mStorage= FirebaseStorage.getInstance().getReference().child("Fotos");
        mProgressDialog= new ProgressDialog(this);
        downloadUrl="";
        imagenPrincipal="";
        fotos= new ArrayList<>();
        articulos= new ArrayList<>();
        Articulos articulosObtener= new Articulos(idArticulo);
        Call<ArticulosListResult> call = nixService.articulosServicio(articulosObtener);
        call.enqueue(new Callback<ArticulosListResult>() {
            @Override
            public void onResponse(Call<ArticulosListResult> call, Response<ArticulosListResult> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CrearPaquetesDatos.this, "Articulos Obtenidos correctamente", Toast.LENGTH_SHORT).show();
                    articulosList=response.body().articulos;

                    for (Articulos art: articulosList){
                        arts.add("Tipo de cantidad: "+art.getPrecioPor()+" Articulo: "+art.getNombre());
                    }
                    itemsArray = new String[arts.size()];
                    itemsArray = arts.toArray(itemsArray);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            getBaseContext(),R.layout.texto_municipios,itemsArray
                    );
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
                    spinner.setAdapter(spinnerArrayAdapter);

                }
                else{
                    Toast.makeText(CrearPaquetesDatos.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                    Log.i("errorArticulos",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ArticulosListResult> call, Throwable t) {

            }
        });

        cantidad= findViewById(R.id.cantidad);
        nombre= findViewById(R.id.nombre);
        precio=findViewById(R.id.costo);
        descripcion= findViewById(R.id.descripcion);
        spinner= findViewById(R.id.spinner);
        terminado=findViewById(R.id.servicioTerminado);

        init();
        articulos_add = findViewById(R.id.articulo_necesario);
        articulos_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tbrow = new TableRow(getApplicationContext());
                tbrow.setId(View.generateViewId());
                TextView t1v = new TextView(getApplicationContext());
                String canti=cantidad.getText().toString();
                t1v.setText(canti);
                t1v.setTextColor(Color.WHITE);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t3v = new TextView(getApplicationContext());
                String arti =spinner.getSelectedItem().toString();
                t3v.setText(arti);
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                stk.addView(tbrow);
                articulos.add(canti+" "+arti);
            }
        });

        terminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setTitle("Creando paquete...");
                mProgressDialog.setMessage("Por favor espere");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            String name= nombre.getText().toString();
            String price= precio.getText().toString();
            String desc= descripcion.getText().toString();
            Paquetes paquetes = new Paquetes(name, desc, price, imagenPrincipal, idArticulo);
                Call<PaqueteResult> call = nixService.nuevoPaquete(paquetes);
                call.enqueue(new Callback<PaqueteResult>() {
                    @Override
                    public void onResponse(Call<PaqueteResult> call, Response<PaqueteResult> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(CrearPaquetesDatos.this, "Crear paquete correcto", Toast.LENGTH_SHORT).show();
                            paque= response.body().paquete;
                            if (!fotos.isEmpty()){
                                for (String imagenes: fotos){
                                    ImagenPaquete imagenEventos1 = new ImagenPaquete(imagenes,paque.getId());
                                    imagenPaquete.add(imagenEventos1);
                                }
                                Call<ResponseBody> callDos = nixService.imagenPaquete(imagenPaquete);
                                callDos.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                           // Toast.makeText(CrearPaquetesDatos.this, "Imagenes añadidas correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(CrearPaquetesDatos.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                            Log.i("errorImag",response.errorBody().toString());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            if (!articulos.isEmpty()){
                                for (String articuloPaq: articulos){
                                    PaqueteArticulo paqArt = new PaqueteArticulo(paque.getId(),articuloPaq);
                                    paqueteArticulo.add(paqArt);
                                }
                                Call<ResponseBody> callTres = nixService.paqueteArticulo(paqueteArticulo);
                                callTres.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){

                                        }
                                        else{
                                            Toast.makeText(CrearPaquetesDatos.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                            Log.i("errorArt",response.errorBody().toString());
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
                            Toast.makeText(CrearPaquetesDatos.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaqueteResult> call, Throwable t) {
                    mProgressDialog.dismiss();
                    }
                });
            
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }
    public void init() {
        stk = (TableLayout) findViewById(R.id.table_main);
        stk.setGravity(Gravity.CENTER);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("  Cantidad  ");
        tv0.setTextSize(20);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv2 = new TextView(this);
        tv2.setText("  Articulo  ");
        tv2.setTextSize(20);
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);


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
            if (length < 7) {
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
                            Toast.makeText(CrearPaquetesDatos.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CrearPaquetesDatos.this, "Subida Exitosa", Toast.LENGTH_SHORT).show();
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
