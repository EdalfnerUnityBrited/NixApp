package com.example.nixapp.UI.usuario.misEventos;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.creadorInvitaciones.CreadorDeInvitaciones;
import com.example.nixapp.UI.welcome.MainActivity;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosResult;
import com.facebook.AccessToken;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareButtonBase;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearEvento extends AppCompatActivity implements View.OnClickListener {

    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_INTENT=1;
    DatePickerDialog picker;
    EditText eTextFecha, eTextHora, nombreEvento, lugarEvento, descripcionEvento, cupoEvento, coverEvento, descripcionEventoEmail;
    private ArrayList<EventosItems> mEventsList;
    private EventosAdapter mAdapter;
    TimePickerDialog picker2;
    Date currentTime = Calendar.getInstance().getTime();
    List<String> fotos;

    Switch simpleSwitch1;
    CheckBox cover;
    TextView correosAgregados;
    String downloadUrl, imagenPrincipal;
    NixService nixService;
    NixClient nixClient;
    int privacidad, categoria_evento, dia, ano, mes;
    String clickedName;
    Button terminar, insertar, enables, info, imagen, catalogo,botonEmail,buscar_imagen, fakecompartir,crear_invitacion;
    int cupo;
    boolean correoagregado = false, imagen_lista = false;
    int ApiActivada = 0;
    Uri imagen_enviar;
    ImageView InvitacionSeleccionada;
    ShareButton shareButton;
    RadioButton r1,r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
        retrofitinit();
        iniciarcomponentes();

        terminar.setOnClickListener(this);
        //////////////////////
        mStorage= FirebaseStorage.getInstance().getReference().child("Fotos");
        mProgressDialog= new ProgressDialog(this);
        eTextFecha.setInputType(InputType.TYPE_NULL);
        eTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                final Calendar cldr = Calendar.getInstance();
                dia = cldr.get(Calendar.DAY_OF_MONTH);
                mes = cldr.get(Calendar.MONTH);
                ano = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CrearEvento.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eTextFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, ano, mes, dia);
                picker.show();
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        eTextHora.setInputType(InputType.TYPE_NULL);
        eTextHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale2 = getResources().getConfiguration().locale;
                Locale.setDefault(locale2);
                final Calendar cldr2 = Calendar.getInstance();
                int hour = cldr2.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr2.get(Calendar.MINUTE);
                // time picker dialog
                picker2 = new TimePickerDialog(CrearEvento.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                eTextHora.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker2.show();
            }
        });

        initList();

        Spinner spinners = findViewById(R.id.spinnerSimple);

        mAdapter = new EventosAdapter(this, mEventsList);
        spinners.setAdapter(mAdapter);

        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventosItems clickedItem = (EventosItems) parent.getItemAtPosition(position);
                categoria_evento= position;
                clickedName = clickedItem.getEventoName();
                Toast.makeText(CrearEvento.this, clickedName + " seleccionada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        simpleSwitch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSwitch1, statusSwitch2;
                if (simpleSwitch1.isChecked()) {
                    info.setVisibility(View.VISIBLE);
                    enables.setVisibility(View.VISIBLE);
                    Toast.makeText(CrearEvento.this, "Ve al catalogo para ver los servicios", Toast.LENGTH_SHORT).show();
                }
                else {
                    info.setVisibility(View.GONE);
                    enables.setVisibility(View.GONE);
                }
            }
        });

        cover = (CheckBox) findViewById(R.id.cover);
        final EditText cover_val = findViewById(R.id.cover_valor);

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSwitch1, statusSwitch2;
                if (cover.isChecked()) {
                    cover_val.setVisibility(View.VISIBLE);
                    Toast.makeText(CrearEvento.this, "Agrega el cover", Toast.LENGTH_SHORT).show();
                }
                else {
                    cover_val.setText("");
                    cover_val.setVisibility(View.GONE);
                }
            }
        });

        final TextView mostrarCorreos = findViewById(R.id.mostrarCorreos);
        final EditText correos = findViewById(R.id.correos);


        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correos.getText().toString().isEmpty()) {
                    Toast.makeText(CrearEvento.this, "Agrega algun correo porfavor", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(validarEmail(correos.getText().toString()))
                    {
                        mostrarCorreos.setText(mostrarCorreos.getText()+"\n" + correos.getText());
                        correoagregado = true;
                        correos.setText("");
                    }
                    else
                    {
                        Toast.makeText(CrearEvento.this, "Eso no es un correo -.-'", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /////////////////////CREAR INVITACIÓN
        crear_invitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irInvitacionIntent = new Intent(CrearEvento.this, CreadorDeInvitaciones.class);
                startActivity(irInvitacionIntent);
            }
        });
        /////////////////////Buscar Imagen
        buscar_imagen = (Button) findViewById(R.id.buttonBuscarInvitacion);
        InvitacionSeleccionada = findViewById(R.id.verInvitacion);
        InvitacionSeleccionada.setVisibility(View.GONE);
        buscar_imagen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                agregarImagen();
            }
        });
        ///////////////////// Enviar Correos
        /*imagen_enviar = imageUri;
                    imagen_lista = true;*/
        descripcionEventoEmail = findViewById(R.id.descripcion);
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
                    Toast.makeText(CrearEvento.this, "Ingresa la invitacion para continuar", Toast.LENGTH_SHORT).show();
                }
                else if(correoagregado== false)
                {
                    Toast.makeText(CrearEvento.this, "Ingresa minimo una direccion de correo para poder poder enviar la invtacion", Toast.LENGTH_LONG).show();
                }
                else
                {
                    enviarEmail();
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
                    Toast.makeText(getApplicationContext(),"No tienes tu cuenta vinculada para compartir con facebook" ,Toast.LENGTH_LONG).show();

                }
                else if(imagen_enviar == null)
                {
                    Toast.makeText(getApplicationContext(),"No has elegido la invitacion para compartir" ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharePhoto photo = new SharePhoto.Builder()
                            .setImageUrl(Uri.parse(imagen_enviar.toString()))
                            .build();
                    SharePhotoContent contents = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareButton.setShareContent(contents);
                }

            }
        });

    }
    private void enviarEmail(){
        //Instanciamos un Intent del tipo ACTION_SEND
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        //Aqui definimos la tipologia de datos del contenido dle Email en este caso text/html
        emailIntent.setType("text/html");
        // Indicamos con un Array de tipo String las direcciones de correo a las cuales enviar
        String direcciones = correosAgregados.getText().toString();
        String[] correos = direcciones.split("\n");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, correos);
        // Aqui definimos un titulo para el Email
        emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "Invitacion del evento: " + nombreEvento.getText());
        // Aqui definimos un Asunto para el Email
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitacion al evento: '"+nombreEvento.getText()+"' Te esperamos...");
        // Aqui obtenemos la referencia al texto y lo pasamos al Email Intent
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, descripcionEventoEmail.getText());
        //Agregar una imagen
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagen_enviar.toString()));
        try {
            //Enviamos el Correo iniciando una nueva Activity con el emailIntent.
            startActivity(Intent.createChooser(emailIntent, "Enviar Correo..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No hay ningun cliente de correo instalado.", Toast.LENGTH_SHORT).show();
        }
    }

    ///////////////////////////////////////////
    public void agregarImagen()
    {
        ApiActivada = 4;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                1);

    }
    /////////////////////////////
    private void iniciarcomponentes() {
        r1=findViewById(R.id.publico);
        r2=findViewById(R.id.privado);
        terminar = findViewById(R.id.eventoTerminado);
        eTextFecha=(EditText) findViewById(R.id.fecha);
        insertar = findViewById(R.id.correoAgregado);
        simpleSwitch1 = (Switch) findViewById(R.id.servicios);
        enables = findViewById(R.id.buttonIrCatalogo);
        info = findViewById(R.id.info);
        eTextHora=(EditText) findViewById(R.id.hora_evento);
        nombreEvento= findViewById(R.id.nomb);
        lugarEvento= findViewById(R.id.direcc);
        cupoEvento= findViewById(R.id.cupo);
        coverEvento=findViewById(R.id.cover_valor);
        crear_invitacion = (Button) findViewById(R.id.buttonIrInvitacion);
        descripcionEvento= findViewById(R.id.descripcion);
        imagen= findViewById(R.id.buttonImagen);
        catalogo=findViewById(R.id.buttonIrCatalogo);
        catalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(CrearEvento.this, MainActivity.class);
                startActivity(i);
            }
        });
        downloadUrl="";
        imagenPrincipal="";
        fotos= new ArrayList<>();

    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void initList() {
        mEventsList = new ArrayList<>();
        mEventsList.add(new EventosItems("Elige una categoria", R.drawable.select_some));
        mEventsList.add(new EventosItems("Compromisos", R.drawable.compromisos));
        mEventsList.add(new EventosItems("Mega Eventos", R.drawable.mega));
        mEventsList.add(new EventosItems("Galas", R.drawable.galas));
        mEventsList.add(new EventosItems("Empresariales", R.drawable.empresariales));
        mEventsList.add(new EventosItems("Festejos", R.drawable.festejos));
        mEventsList.add(new EventosItems("Religiosos", R.drawable.religiosos));
    }

    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    @Override
    public void onClick(View v) {
        int privacidad= privacidad();
        String nombre = nombreEvento.getText().toString();
        String lugar=lugarEvento.getText().toString();
        String fecha= eTextFecha.getText().toString();
        String hora=(eTextHora.getText().toString())+":00";
        String cupo= cupoEvento.getText().toString();
        String precio= coverEvento.getText().toString();
        final List<ImagenEventos> imagenEventos= new ArrayList<>();


        String[] fechanueva= fecha.split("/");
                dia=Integer.parseInt(fechanueva[0]);
                mes= Integer.parseInt(fechanueva[1]);
                ano=Integer.parseInt(fechanueva[2]);
                fecha= (ano+"-"+mes+"-"+dia);
        int cover;
        int numCupo=0;
        String descripcion=descripcionEvento.getText().toString();
        if (nombre.isEmpty()){
            nombreEvento.setError("Ingrese Nombre para el evento");
        }
        if (lugar.isEmpty()){
            lugarEvento.setError("Ingrese lugar");
        }
        if (cupo.isEmpty()){
            cupoEvento.setError("Ingrese cupo");
        }
        else{
            numCupo= Integer.parseInt(cupo);
        }
        if (precio.isEmpty()){
            cover=0;
        }else{
            cover=Integer.parseInt(precio);
        }
        if (descripcion.isEmpty()){
            descripcionEvento.setError("Ingrese descripción");
        }
        if (categoria_evento==0){
            Toast.makeText(CrearEvento.this, "Elija una categoria", Toast.LENGTH_SHORT).show();
        }
        else{
            boolean fechacorrecta=verificarFecha(dia, mes, ano);
            if (!fechacorrecta){
                final Eventos requestSample = new Eventos(nombre,privacidad,categoria_evento,fecha,hora,lugar,descripcion,numCupo,cover, imagenPrincipal);
                Call<EventosResult> call= nixService.eventos(requestSample);
                call.enqueue(new Callback<EventosResult>() {
                    @Override
                    public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(CrearEvento.this, "Crear evento correcto", Toast.LENGTH_SHORT).show();
                            Eventos eventos= response.body().eventos;
                            String id= eventos.getId();
                            if (!fotos.isEmpty()){

                                for (String imagenes: fotos){
                                   ImagenEventos image= new ImagenEventos(imagenes.toString(), id);
                                   imagenEventos.add(image);
                                }
                                Call<ResponseBody> callImagen=nixService.image(imagenEventos);
                                callImagen.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(CrearEvento.this, "Imagenes añadidas correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(CrearEvento.this, "Error al añadir las imagenes", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(CrearEvento.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                            try {
                                Log.i("Error",response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<EventosResult> call, Throwable t) {

                    }
                });

            }
        }

    }

    private boolean verificarFecha(int day, int month, int year) {
        int diff=0;
        if (currentTime.getYear()>year){
            eTextFecha.setError("Esa fecha se encuentra en el pasado");
        }
        else{
            if (currentTime.getMonth() > month ||
                    (currentTime.getMonth() == month && currentTime.getDay() > day)) {
                eTextFecha.setError("Esa fecha se encuentra en el pasado");
            }
            else{
                diff=day-currentTime.getDay();
            }
        }


        if (diff<3){
            eTextFecha.setError("Tienes que tener 3 dias de anticipación");
            return true;
        }
        else{

            return false;
        }
    }

    private int privacidad() {
        if (r1.isChecked()){
            return 0;
        }
        if (r2.isChecked()){
            return 1;
        }
        return 0;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                            Toast.makeText(CrearEvento.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CrearEvento.this, "Subida Exitosa", Toast.LENGTH_SHORT).show();
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
                    imagen_enviar = imageUri;
                    imagen_lista = true;
                    Glide.with(this).load(imagen_enviar).into(InvitacionSeleccionada);
                    InvitacionSeleccionada.setVisibility(View.VISIBLE);
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

