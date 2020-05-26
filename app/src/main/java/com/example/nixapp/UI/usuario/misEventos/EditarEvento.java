package com.example.nixapp.UI.usuario.misEventos;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.ViewPagerAdapter;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Plantillas;
import com.example.nixapp.UI.usuario.misEventos.BusquedaServicios.BuscarServicios;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosResult;
import com.example.nixapp.conn.results.ImagenResult;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarEvento extends AppCompatActivity implements View.OnClickListener {

    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_INTENT=1;
    DatePickerDialog picker;
    EditText eTextFecha, eTextHora, nombreEvento, lugarEvento, descripcionEvento, cupoEvento, coverEvento, descripcionEventoEmail, cover_val;
    private ArrayList<EventosItems> mEventsList;
    private EventosAdapter mAdapter;
    TimePickerDialog picker2;
    Calendar currentTime = Calendar.getInstance();
    List<String> fotos;
    Eventos eventos;

    Switch simpleSwitch1;
    CheckBox cover;
    TextView correosAgregados;
    String downloadUrl, imagenPrincipal="";
    NixService nixService;
    NixClient nixClient;
    int privacidad, categoria_evento, dia, ano, mes;
    String clickedName, municipio, id;
    public static int id_evento;
    Button terminar, insertar, enables, info, imagen, catalogo,botonEmail,buscar_imagen, fakecompartir,crear_invitacion, agregar_imagen, quitar_imagen,checardireccion,info_cotrataciones;
    int cupo;
    boolean correoagregado = false, imagen_lista = false,picadoChecarDireccion=false, igualdadDireccionMunicipio = false;
    int ApiActivada = 0;
    Uri imagen_enviar;
    ImageView InvitacionSeleccionada;
    ShareButton shareButton;
    RadioButton r1,r2;
    Spinner spinner,spinners;
    List<ImagenEventos> eventosUsuario;
    ViewPagerAdapter viewPagerAdapter;
    AlertDialog.Builder dialogo1,informacion;
    ViewPager viewPager;
    String[] Minicipios;
    int contador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);
        retrofitinit();
        iniciarcomponentes();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Editar Evento");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        terminar.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MisEventos.class);
                startActivity(intent);
                EditarEvento.this.overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
            }
        });
        //////////////////////
        mStorage= FirebaseStorage.getInstance().getReference().child("Fotos");
        mProgressDialog= new ProgressDialog(this);
        eTextFecha.setInputType(InputType.TYPE_NULL);
        ////////////////////////////
        info_cotrataciones = findViewById(R.id.info);
        info_cotrataciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new AlertDialog.Builder(EditarEvento.this);
                informacion.setTitle("Informacion acerca de las cotizaciones:");
                informacion.setMessage("Aqui es donde se podra cotizar y contratar diferentes servicios para tu evento, podras guardar ver todo lo que te ofrece cada uno y saber el aproximado de lo que te saldria contratarlo");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Leido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        ///////////////////////////
        checardireccion = (Button) findViewById(R.id.checardireccion_editar);
        checardireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picadoChecarDireccion = true;
                if (lugarEvento.getText() != null && spinner.getSelectedItem()!=null)
                    checarIgualdadDireccionMunicipio(lugarEvento.getText().toString(),spinner.getSelectedItem().toString());
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
                picker2 = new TimePickerDialog(EditarEvento.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String minutos;
                                String horas;
                                if(sMinute < 10) {
                                    minutos = "0" + String.valueOf(sMinute);
                                    eTextHora.setText(sHour + ":" + minutos);

                                }
                                else if(sHour < 10)
                                {
                                    horas = "0" + String.valueOf(sHour);
                                    eTextHora.setText(horas + ":" + sMinute);
                                }
                                else if(sHour <0 && sMinute< 0)
                                {
                                    minutos = "0" + String.valueOf(sMinute);
                                    horas = "0" + String.valueOf(sHour);
                                    eTextHora.setText(horas + ":" + minutos);
                                }
                                else
                                {
                                    eTextHora.setText(sHour + ":" + sMinute);
                                }
                            }
                        }, hour, minutes, true);
                picker2.show();
            }
        });

        initList();

        spinners = findViewById(R.id.spinnerSimple);

        mAdapter = new EventosAdapter(this, mEventsList);
        spinners.setAdapter(mAdapter);

        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventosItems clickedItem = (EventosItems) parent.getItemAtPosition(position);
                categoria_evento= position;
                clickedName = clickedItem.getEventoName();
                Toast.makeText(EditarEvento.this, clickedName + " seleccionada", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditarEvento.this, "Ve al catalogo para ver los servicios", Toast.LENGTH_SHORT).show();
                }
                else {
                    info.setVisibility(View.GONE);
                    enables.setVisibility(View.GONE);
                }
            }
        });



        final TextView mostrarCorreos = findViewById(R.id.mostrarCorreos);
        final EditText correos = findViewById(R.id.correos);


        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correos.getText().toString().isEmpty()) {
                    Toast.makeText(EditarEvento.this, "Agrega algun correo porfavor", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditarEvento.this, "Eso no es un correo -.-'", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ///////////////////////////////////////////
        dialogo1 = new AlertDialog.Builder(EditarEvento.this);
        dialogo1.setTitle("Importante");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                String imagenBorrar=viewPagerAdapter.getImagenes().get(viewPager.getCurrentItem()).getImagen();
                ImagenEventos imagenEventos= new ImagenEventos(imagenBorrar);
                Call<ResponseBody> call = nixService.borrarImagen(imagenEventos);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(EditarEvento.this, "Imagen borrada", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(EditarEvento.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                            try {
                                Log.i("Error",response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(EditarEvento.this, "Error en la conexion", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //cancelar();
            }
        });
        //////////////////////////////////////////
        agregar_imagen = findViewById(R.id.agregar_ima);
        agregar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        agregarImagen();
                    }
        });
        quitar_imagen = findViewById(R.id.quitar_ima);
        quitar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo1.setMessage("¿Desea eliminar el elemento: " + (viewPager.getCurrentItem()+1) + "?");
                dialogo1.show();
                //viewPagerAdapter.getImagenes().remove(viewPager.getCurrentItem()); Asi se elimina de la vista (se hace antes que de la BD)
                //Neri no se que pase si se borran todas las fotos :o... lo mas seguro es que de error, intentalo <3 (La de sully solo se quita de la lista)
            }
        });
        /////////////////////CREAR INVITACIÓN
        crear_invitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irInvitacionIntent = new Intent(EditarEvento.this, Plantillas.class);
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
                agregarInvitacion();

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
                    Toast.makeText(EditarEvento.this, "Ingresa la invitacion para continuar", Toast.LENGTH_SHORT).show();
                }
                else if(correoagregado== false)
                {
                    Toast.makeText(EditarEvento.this, "Ingresa minimo una direccion de correo para poder poder enviar la invtacion", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"No tienes tu cuenta vinculada para compartir con facebook, ntp puedes compartirla despues" ,Toast.LENGTH_LONG).show();

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


        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.texto_municipios,Minicipios
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.texto_municipios);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                municipio = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                municipio = spinner.getSelectedItem().toString();
            }
        });
    }

    private void checarIgualdadDireccionMunicipio(String direccion,final String municipio) {
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?&address=%1$s+Jalisco+Mexico&key=AIzaSyAPGGYxsJfpi3DY0o11lAR4-Gccfpf3juw",direccion);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Toast.makeText(EditarEvento.this, "ERROR API GEOCODING", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    EditarEvento.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (myResponse.contains(municipio)){
                                igualdadDireccionMunicipio=true;
                                Toast.makeText(EditarEvento.this, "La dirección es correcta", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(EditarEvento.this, "La direccion ingresada no forma parte del municipio elegido", Toast.LENGTH_LONG).show();
                                igualdadDireccionMunicipio=false;
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            Intent intent = new Intent(getApplicationContext(), MisEventos.class);
            startActivity(intent);
            this.overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setToolbarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }
    private void enviarEmail(){
        //Instanciamos un Intent del tipo ACTION_SEND
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        //Aqui definimos la tipologia de datos del contenido dle Email en este caso text/html
        emailIntent.setType("text/html");
        // Indicamos con un Array de tipo String las direcciones de correo a las cuales enviar
        String direcciones = correosAgregados.getText().toString();
        String[] correos = direcciones.split("\n");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, correos);
        // Aqui definimos un titulo para el Email
        emailIntent.putExtra(Intent.EXTRA_TITLE, "Invitacion del evento: " + nombreEvento.getText());
        // Aqui definimos un Asunto para el Email
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitacion al evento: '"+nombreEvento.getText()+"' Te esperamos...");
        // Aqui obtenemos la referencia al texto y lo pasamos al Email Intent
        emailIntent.putExtra(Intent.EXTRA_TEXT, descripcionEventoEmail.getText());
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

    public void agregarInvitacion()
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

    public void agregarImagen()
    {
        ApiActivada = 5;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                1);

    }
    /////////////////////////////
    private void iniciarcomponentes() {
        id = (String) getIntent().getSerializableExtra("id");
        id_evento= Integer.parseInt(id);
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
        cover = (CheckBox) findViewById(R.id.cover);
        cover_val = findViewById(R.id.cover_valor);
        Minicipios = new String[]{
                "Elige un municipio:",
                "Acatic",
                "Ameca",
                "Arandas",
                "Atotonilco el alto",
                "Chapala",
                "Cocula",
                "El Arenal",
                "El Salto",
                "Guachinango",
                "Guadalajara",
                "Jocotepec",
                "La Barca",
                "Lagos de Moreno",
                "Mascota",
                "Mazamitla",
                "Mezquitic",
                "Puerto Vallarta",
                "San Juan de los Lagos",
                "Tlaquepaque",
                "Sayula",
                "Tala",
                "Tapalpa",
                "Tequila",
                "Tlajomulco de Zuñiga",
                "Tonala",
                "Tototlan",
                "Zapopan",
                "Zapotlanejo"
        };

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSwitch1, statusSwitch2;
                if (cover.isChecked()) {
                    cover_val.setVisibility(View.VISIBLE);
                    Toast.makeText(EditarEvento.this, "Agrega el cover", Toast.LENGTH_SHORT).show();
                }
                else {
                    cover_val.setText("");
                    cover_val.setVisibility(View.GONE);
                }
            }
        });
        catalogo=findViewById(R.id.buttonIrCatalogo);
        catalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(EditarEvento.this, BuscarServicios.class);
                i.putExtra("categoria",categoria_evento);
                startActivity(i);
            }
        });
        downloadUrl="";
        imagenPrincipal="";
        fotos= new ArrayList<>();
        Busqueda busqueda= new Busqueda(id);
        Call<EventosResult> call= nixService.buscarEventoId(busqueda);
        call.enqueue(new Callback<EventosResult>() {
            @Override
            public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                if(response.isSuccessful()){
                    eventos=response.body().eventos;
                    eTextFecha.setText(eventos.getFecha());
                    eTextHora.setText(eventos.getHora());
                    nombreEvento.setText(eventos.getNombre_evento());
                    descripcionEvento.setText(eventos.getDescripcion());
                    lugarEvento.setText(eventos.getLugar());
                    imagenPrincipal=eventos.getFotoPrincipal();
                    spinners.setSelection(eventos.getCategoria_evento());
                    cupoEvento.setText(String.valueOf(eventos.getCupo()));
                    if(eventos.getCover() != 0)
                    {
                        cover.setChecked(true);
                        cover_val.setVisibility(View.VISIBLE);
                        cover_val.setText(String.valueOf(eventos.getCover()));

                    }
                    else
                    {
                        cover.setChecked(false);
                        cover_val.setText("");
                        cover_val.setVisibility(View.GONE);
                    }
                    if(eventos.getPrivacidad() == 0)
                    {
                        r1.setChecked(true);
                        r2.setChecked(false);
                    }
                    else
                    {
                        r2.setChecked(true);
                        r1.setChecked(false);
                    }
                    for (int i = 0; i < Minicipios.length ; i++)
                    {

                        if(Minicipios[i].equals(eventos.getMunicipio()))
                        {
                            contador = i;
                        }
                    }
                    spinner.setSelection(contador);
                    final Eventos event =new Eventos(eventos.getNombre_evento());
                    Call<ImagenResult> calls = nixService.buscarImagenes(event);
                    calls.enqueue(new Callback<ImagenResult>() {
                        @Override
                        public void onResponse(Call<ImagenResult> call, Response<ImagenResult> response) {
                            if(response.isSuccessful())
                            {

                                eventosUsuario = response.body().imagenEventos;
                                viewPager = (ViewPager) findViewById(R.id.viewPager);
                                if(eventosUsuario.isEmpty())
                                {
                                    //Toast.makeText(getApplicationContext(),"Esta vacio...",Toast.LENGTH_LONG).show();

                                    eventosUsuario.add(new ImagenEventos("https://pbs.twimg.com/media/DqiOx30WkAEcWEg.jpg","1"));
                                }
                                else
                                {
                                    //Toast.makeText(getApplicationContext(),String.valueOf(eventosUsuario.size()),Toast.LENGTH_LONG).show();

                                }
                                viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(),eventosUsuario);
                                viewPager.setAdapter(viewPagerAdapter);




                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),response.errorBody().toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ImagenResult> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Error en el intento",Toast.LENGTH_LONG).show();
                        }
                    });


                }
                else{
                    Toast.makeText(EditarEvento.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                    Log.i("error",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<EventosResult> call, Throwable t) {
                Toast.makeText(EditarEvento.this, "Error en la llamada", Toast.LENGTH_SHORT).show();
            }
        });
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
        int privacidad = privacidad();
        String nombre = nombreEvento.getText().toString();
        String lugar = lugarEvento.getText().toString();
        String fecha = eTextFecha.getText().toString();
        String hora = (eTextHora.getText().toString());
        String cupo = cupoEvento.getText().toString();
        String precio = coverEvento.getText().toString();
        final List<ImagenEventos> imagenEventos = new ArrayList<>();

        try {
            String[] fechanueva = fecha.split("/");
            dia = Integer.parseInt(fechanueva[0]);
            mes = Integer.parseInt(fechanueva[1]);
            ano = Integer.parseInt(fechanueva[2]);
            fecha = (ano + "-" + mes + "-" + dia);
        } catch (Exception e) {
            fecha = eventos.getFecha();
        }

        int cover;
        int numCupo = 0;
        String descripcion = descripcionEvento.getText().toString();
        if (nombre.isEmpty()) {
            nombre = eventos.getNombre_evento();
        }
        if (lugar.isEmpty()) {
            lugar = eventos.getLugar();
        }
        if (cupo.isEmpty()) {
            numCupo = eventos.getCupo();
        } else {
            numCupo = Integer.parseInt(cupo);
        }
        if (precio.isEmpty()) {
            cover = eventos.getCover();
        } else {
            cover = Integer.parseInt(precio);
        }
        if (descripcion.isEmpty()) {
            descripcion = eventos.getDescripcion();
        }
        if (categoria_evento == 0) {
            categoria_evento = eventos.getCategoria_evento();
        }
        if (spinner.getSelectedItem().toString().equals("Elige un municipio:")) {
            municipio = eventos.getMunicipio();
        }
        if (igualdadDireccionMunicipio != true || picadoChecarDireccion != true) {
            Toast.makeText(this, "Falta verificar la dirección", Toast.LENGTH_SHORT).show();
        }
        else{
            boolean fechacorrecta = verificarFecha(dia, mes, ano);
            if (!fechacorrecta) {
                final Eventos requestSample = new Eventos(id, nombre, privacidad, categoria_evento, fecha, hora, lugar, descripcion, numCupo, cover, imagenPrincipal, municipio);
                Call<ResponseBody> call = nixService.actualizarEvento(requestSample);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditarEvento.this, "Cambios actializados", Toast.LENGTH_SHORT).show();

                            if (!fotos.isEmpty()) {

                                for (String imagenes : fotos) {
                                    ImagenEventos image = new ImagenEventos(imagenes.toString(), id);
                                    imagenEventos.add(image);
                                }
                                Call<ResponseBody> callImagen = nixService.image(imagenEventos);
                                callImagen.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(EditarEvento.this, "Imagenes añadidas correctamente", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditarEvento.this, "Error al añadir las imagenes", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MisEventos.class);
                            startActivity(intent);
                            EditarEvento.this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                        } else {
                            Toast.makeText(EditarEvento.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                            try {
                                Log.i("Error", response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }

        }
    }

    private boolean verificarFecha(int day, int month, int year) {
        int diff=0;
        if (currentTime.get(Calendar.YEAR)>year){
            eTextFecha.setError("Esa fecha se encuentra en el pasado");
            return true;
        }
        else{
            if ((currentTime.get(Calendar.MONTH) +1)> month ||
                    ((currentTime.get(Calendar.MONTH)+1) == month && currentTime.get(Calendar.DAY_OF_MONTH) > day)) {
                eTextFecha.setError("Esa fecha se encuentra en el pasado");
                return true;
            }
            else{
                if((currentTime.get(Calendar.MONTH) +1) == month)
                {
                    diff=day-currentTime.get(Calendar.DAY_OF_MONTH);
                    if (diff<3){
                        eTextFecha.setError("Tienes que tener 3 dias de anticipación");
                        return true;
                    }
                }
            }
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
                            Toast.makeText(EditarEvento.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditarEvento.this, "Subida Exitosa", Toast.LENGTH_SHORT).show();
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
                                    ImagenEventos image= new ImagenEventos(downloadUrl, id);
                                    viewPagerAdapter.getImagenes().add(viewPagerAdapter.getImagenes().size()+1,image);//Se la agregas a la lista del adapter solo para que se vea....
                                    Call<ResponseBody> call = nixService.imagenUna(image);
                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()){
                                                Toast.makeText(EditarEvento.this, "Imagen añadida correctamente", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(EditarEvento.this, "Error", Toast.LENGTH_SHORT).show();
                                                Log.i("error",response.errorBody().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(EditarEvento.this, "Error en la ruta", Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
        else if(ApiActivada == 4)
        {
            super.onActivityResult(requestCode, resultCode, data);
            final Uri imageUri = data.getData();

            imagen_enviar = imageUri;
            imagen_lista = true;
            Glide.with(this).load(imagen_enviar).into(InvitacionSeleccionada);
            InvitacionSeleccionada.setVisibility(View.VISIBLE);
            ApiActivada = 0;
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mProgressDialog.setTitle("Subiendo...");

                    mProgressDialog.setMessage("Subiendo foto a firebase");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    Uri uri = data.getData();
                    viewPager.setVisibility(View.VISIBLE);
                    final StorageReference filePath = mStorage.child(uri.getLastPathSegment());
                    final UploadTask uploadTask = filePath.putFile(uri);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = e.toString();
                            Toast.makeText(EditarEvento.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditarEvento.this, "Subida Exitosa", Toast.LENGTH_SHORT).show();
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
                                    ImagenEventos image= new ImagenEventos(downloadUrl, id);
                                    Call<ResponseBody> call = nixService.imagenUna(image);
                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()){
                                                Toast.makeText(EditarEvento.this, "Imagen añadida correctamente", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(EditarEvento.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                                try {
                                                    Log.i("Error",response.errorBody().string().toString());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(EditarEvento.this, "Error en la ruta", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    fotos.add(downloadUrl);
                                    mProgressDialog.dismiss();
                                    viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(),eventosUsuario);
                                    viewPager.setAdapter(viewPagerAdapter);
                                    contador++;

                                }
                            });


                        }
                    });

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

