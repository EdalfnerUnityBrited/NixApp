package com.example.nixapp.UI.proveedor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.CrearEvento;
import com.example.nixapp.UI.usuario.misEventos.EditarEvento;
import com.example.nixapp.UI.usuario.misEventos.MisEventos;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.facebook.AccessToken;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Compartir_Proveedor extends AppCompatActivity {

    Button buscar_imagen,whats,insertar,fakecompartir,botonEmail;
    ImageView InvitacionSeleccionada,InvitacionSeleccionada2;
    List<Uri> lista = new ArrayList<>();
    boolean imagen_lista = false,correoagregado = false,activado = false;
    EditText correos,users_Nix;
    TextView mostrarCorreos,correosAgregados;
    ShareButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartir__proveedor);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Nuevo Evento");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        buscar_imagen = (Button) findViewById(R.id.buttonBuscarInvitacion);
        InvitacionSeleccionada = findViewById(R.id.verInvitacion);
        InvitacionSeleccionada.setVisibility(View.GONE);
        InvitacionSeleccionada2 = findViewById(R.id.verInvitacion2);
        InvitacionSeleccionada2.setVisibility(View.GONE);
        insertar = findViewById(R.id.correoAgregado);
        correos = findViewById(R.id.correos);
        mostrarCorreos = findViewById(R.id.mostrarCorreos);
        users_Nix = findViewById(R.id.usuariosNix);
        buscar_imagen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                agregarImagen();
            }
        });
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correos.getText().toString().isEmpty()) {
                    Toast.makeText(Compartir_Proveedor.this, "Agrega algun correo porfavor", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(validarEmail(correos.getText().toString()))
                    {
                        String email=correos.getText().toString();
                        mostrarCorreos.setText(mostrarCorreos.getText() + "\n" + email);
                        correos.setText("");
                        correoagregado = true;

                    }
                    else
                    {
                        Toast.makeText(Compartir_Proveedor.this, "Eso no es un correo -.-'", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        whats = findViewById(R.id.Whatsapp);
        whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagen_lista == false)
                {
                    Toast.makeText(getApplicationContext(),"No has elegido la invitacion para compartir" ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    enviarWhats();
                }

            }
        });
        ////////////////////////////////Compartir facebook
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        fakecompartir = findViewById(R.id.face);

        shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setEnabled(true);

        fakecompartir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareButton.performClick();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(AccessToken.getCurrentAccessToken()==null)
                {
                    Toast.makeText(getApplicationContext(),"No tienes tu cuenta vinculada para compartir con facebook, ntp puedes compartirla despues" ,Toast.LENGTH_LONG).show();

                }
                else if(imagen_lista == false)
                {
                    Toast.makeText(getApplicationContext(),"No has elegido la invitacion para compartir" ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    List<SharePhoto> fotos = new ArrayList<>();
                    for (Uri uri: lista) {
                        SharePhoto photo = new SharePhoto.Builder()
                                .setImageUrl(uri)
                                .build();
                        fotos.add(photo);
                    }

                    SharePhotoContent contents = new SharePhotoContent.Builder()
                            .addPhotos(fotos)
                            .build();
                    shareButton.setShareContent(contents);
                }

            }
        });
        ///////////////////////////////
        correosAgregados = findViewById(R.id.mostrarCorreos);
        botonEmail = (Button) findViewById(R.id.EnviarCorreo);
        botonEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Llamamos al metodo enviarEmail
                if(imagen_lista  == false)
                {
                    Toast.makeText(Compartir_Proveedor.this, "Ingresa la invitacion para continuar", Toast.LENGTH_SHORT).show();
                }
                else if(correoagregado== false)
                {
                    Toast.makeText(Compartir_Proveedor.this, "Ingresa minimo una direccion de correo para poder poder enviar la invtacion", Toast.LENGTH_LONG).show();
                }
                else
                {
                    enviarEmail();
                }

            }
        });


    }

    private void enviarEmail(){
        //Instanciamos un Intent del tipo ACTION_SEND
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //Aqui definimos la tipologia de datos del contenido dle Email en este caso text/html
        emailIntent.setType("text/html");
        // Indicamos con un Array de tipo String las direcciones de correo a las cuales enviar
        String direcciones = correosAgregados.getText().toString();
        String[] correos = direcciones.split("\n");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, correos);
        // Aqui definimos un titulo para el Email
        emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "Participacion en un evento Proximo");
        // Aqui definimos un Asunto para el Email
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sere parte de un evento muy pronto, no te lo pierdas!");
        // Aqui obtenemos la referencia al texto y lo pasamos al Email Intent
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Te esperamos!!!");
        //Agregar una imagen
        ArrayList<Uri> imageUriArray = new ArrayList<>();

        for (Uri uris: lista)
        {
            imageUriArray.add(uris);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
        try {
            //Enviamos el Correo iniciando una nueva Activity con el emailIntent.
            startActivity(Intent.createChooser(emailIntent, "Enviar Correo..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No tienes instalado ninguna aplicaciones de correos electronicos.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void enviarWhats(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //Aqui definimos la tipologia de datos del contenido dle Email en este caso text/html
        emailIntent.setType("text/html");
        emailIntent.setPackage("com.whatsapp");
        ArrayList<Uri> imageUriArray = new ArrayList<>();

        for (Uri uris: lista)
        {
            imageUriArray.add(uris);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
        try {
            //Enviamos el Correo iniciando una nueva Activity con el emailIntent.
            startActivity(Intent.createChooser(emailIntent, "Enviar Mensaje..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No tienes instalado Whatsapp.", Toast.LENGTH_SHORT).show();
        }
    }

    public void agregarImagen()
    {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            ClipData clipData = data.getClipData();
            if (clipData != null) {
                lista.clear();
                imagen_lista = true;
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imagen = clipData.getItemAt(i).getUri();
                    try {
                        final InputStream imageStream = getContentResolver().openInputStream(imagen);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imagen_lista = true;
                        if (i == 0 && clipData.getItemCount() > 0) {
                            lista.add(imagen);
                            Glide.with(this).load(imagen).into(InvitacionSeleccionada);
                            InvitacionSeleccionada.setVisibility(View.VISIBLE);
                        } else if (i == 1 && clipData.getItemCount() > 1) {
                            lista.add(imagen);
                            Glide.with(this).load(imagen).into(InvitacionSeleccionada2);
                            InvitacionSeleccionada2.setVisibility(View.VISIBLE);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
        }
        }
    }
}
