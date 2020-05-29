package com.example.nixapp.UI.proveedor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuPrincipalUsuarioProveedor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    TextView nombre, email;
    CircleImageView fotoPerfil, imagenPerfil;
    Usuario usuario;
    String downloadUrl;
    NixService nixService;
    NixClient nixClient;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_INTENT=1;
    Date currentTime = Calendar.getInstance().getTime();
    String date;
    boolean cambio = true,cambio2 = true;
    EditText nombreEdit, telefono, apP, apM, correo, passwordUser, passwordConfirm;
    TextView fecha, saludo;
    Button cambioDato, passwordCambio;
    private ProfileTracker profileTracker;
    CallbackManager callbackManager;
    LoginButton loginButton = null;
    AccessTokenTracker accessTokenTracker;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account ;
    SignInButtonImpl signInButton;
    Button salir, fake, recaptcha;
    TextView nombreG, apG, correoG, cumpleG,given;
    ImageView fotoperfil;
    int ApiActivada = 0;
    int RC_SIGN_IN = 9001;
    boolean imagen_agregada = false;
    Button VGoo;
    ////////////////////////////////////
    private static final String TAG = "MainActivity";
    private TextView mDisplayDate=null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int ano, mes, dia;
    AlertDialog.Builder dialogo1;

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
        nombreEdit= findViewById(R.id.nombre_user);
        nombreEdit.setText(usuario.name);
        telefono= findViewById(R.id.telefono_usuario);
        telefono.setText(usuario.telefono);
        fecha= findViewById(R.id.fecha_nacimiento);
        fecha.setText(usuario.fechaNac);
        correo=findViewById(R.id.email_user);
        correo.setText(usuario.email);
        correo.setEnabled(false);
        correo.setBackground(null);
        apP = findViewById(R.id.ap_pat_user);
        apM = findViewById(R.id.ap_mat_user);
        cambioDato=findViewById(R.id.buttonGuardar);
        saludo=findViewById(R.id.nombre_inicial);
        saludo.setText("Hola, "+usuario.name);
        passwordUser= findViewById(R.id.password_usuario);
        passwordConfirm= findViewById(R.id.confirmacion_contra);
        passwordCambio= findViewById(R.id.buttonPass);
        imagenPerfil= findViewById(R.id.profile_image);
        Glide.with(imagenPerfil)
                .load(usuario.fotoPerfil)
                .into(imagenPerfil);
        passwordCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contra= passwordUser.getText().toString();
                String confContra=passwordConfirm.getText().toString();
                String nombreUser= usuario.name;
                boolean verificarContraseña=verificarContraseña(contra,confContra);
                if (!verificarContraseña){
                    final Usuario requestSample = new Usuario(confContra, nombreUser, contra);
                    Call<ResponseBody> call = nixService.pass(requestSample);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(MenuPrincipalUsuarioProveedor.this, "Actualización Exitosa", Toast.LENGTH_SHORT).show();
                                passwordUser.setError(null);
                                passwordConfirm.setError(null);
                            }
                            else{
                                Toast.makeText(MenuPrincipalUsuarioProveedor.this, "Error en los datos", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });
        final String fechaUsuario= usuario.fechaNac;
        String[] fechaNacimiento=fechaUsuario.split("-");
        ano=Integer.parseInt(fechaNacimiento[0]);
        mes=Integer.parseInt(fechaNacimiento[1]);
        dia=Integer.parseInt(fechaNacimiento[2]);
        cambioDato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = nombreEdit.getText().toString();
                String telefonoUsuario = telefono.getText().toString();
                String ap = apP.getText().toString();
                String am = apM.getText().toString();
                if (nombreUsuario.isEmpty()){
                    nombreEdit.setError("Llene el nombre");
                }
                else if (telefonoUsuario.isEmpty()){
                    telefono.setError("Llene el nombre");
                }
                else{
                    date=fecha.getText().toString();
                    boolean verificarEdad= verificarCumpleaños(ano, mes, dia);
                    if (!verificarEdad){
                        date=fecha.getText().toString();
                        if (ap.isEmpty()){
                            ap=usuario.apellidoP;
                        }
                        if (am.isEmpty()){
                            am=usuario.apellidoM;
                        }
                        final Usuario requestSample = new Usuario(nombreUsuario,ap,am,date,telefonoUsuario);
                        Call<ResponseBody> call = nixService.datos(requestSample);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(MenuPrincipalUsuarioProveedor.this, "Actualización Exitosa", Toast.LENGTH_SHORT).show();
                                    mDisplayDate.setError(null);
                                    telefono.setError(null);
                                    nombre.setError(null);
                                }
                                else{
                                    Toast.makeText(MenuPrincipalUsuarioProveedor.this, "Error en los datos", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });
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
        mDisplayDate = (TextView) findViewById(R.id.fecha_nacimiento);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MenuPrincipalUsuarioProveedor.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();



            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "-" + day + "-" + year);
                ano=year;
                mes=month;
                dia=day;
                date = year + "-" + month + "-" + day;
                mDisplayDate.setText(date);
            }
        };

        final EditText pass = findViewById(R.id.password_usuario);
        final EditText pass_conf = findViewById(R.id.confirmacion_contra);
        final View row = findViewById(R.id.confirmacion);
        final View view2 = findViewById(R.id.view2);
        view2.setVisibility(View.GONE);

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true)
                {
                    pass.setHint("Nueva Contraseña");
                }
                else
                {
                    pass.setHint("¿Desea cambiar su contraseña?");
                }
            }

        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String text = pass.getText().toString();
                if(text.equals(""))
                {
                    animation(row);
                    pass_conf.setText("");
                    view2.setVisibility(View.GONE);
                    cambio = true;

                }
                else{
                    if(cambio == true)
                    {
                        animation1(row);
                        view2.setVisibility(View.VISIBLE);
                        cambio = false;
                    }

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        //////////////////////////////////////////////////////////////////////// //
        final EditText nombre = findViewById(R.id.nombre_user);
        final View rowapP = findViewById(R.id.apellido_paterno);
        final View rowapM = findViewById(R.id.apellido_materno);
        final View invi = findViewById(R.id.inv1);
        final View invi2 = findViewById(R.id.invi2);

        nombre.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true)
                {
                    nombre.setHint("Nuevo nombre");
                }
                else
                {
                    nombre.setHint("¿Quieres Editar tu nombre?");
                }
            }

        });

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String text2 = nombre.getText().toString();
                if(text2.equals(""))
                {

                    animation(rowapP);
                    animation(rowapM);
                    apM.setText("");
                    apM.setHint(usuario.apellidoM);
                    apP.setText("");
                    apP.setHint(usuario.apellidoP);
                    invi.setVisibility(View.GONE);
                    invi2.setVisibility(View.GONE);
                    cambio2 = true;

                }
                else{
                    if(cambio2 == true)
                    {
                        animation1(rowapP);
                        animation1(rowapM);
                        apM.setText("");
                        apM.setHint(usuario.apellidoM);
                        apP.setText("");
                        apP.setHint(usuario.apellidoP);
                        invi.setVisibility(View.VISIBLE);
                        invi2.setVisibility(View.VISIBLE);
                        cambio2 = false;
                    }

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });


        //////////////////////////////////Vericar y cambiar los botones de vinculacion
        //Facebook Stuff Inicio de Sesion
        callbackManager = CallbackManager.Factory.create();
        //Boton falso
        loginButton = (LoginButton) findViewById(R.id.faceprueba);
        loginButton.setVisibility(View.INVISIBLE);
        fake = findViewById(R.id.Vface);
        fake.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginButton.performClick();

            }
        });
        ///////////////////////////////////////////////////
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("user_birthday");
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged (Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {

                }
            }
        };
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(MenuPrincipalUsuarioProveedor.this,"Vinculacion con facebook exitosa",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(MenuPrincipalUsuarioProveedor.this,"Intento Cancelado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(MenuPrincipalUsuarioProveedor.this,"Error al intentar iniciar sesion",Toast.LENGTH_SHORT).show();
            }
        });
        final Button VFace = findViewById(R.id.Vface);
        VGoo = findViewById(R.id.Vgoogle);
        //PA salir de la sincronizacion de facebook LoginManager.getInstance().logOut();
        //AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user clicks on facebook logout
                    VFace.setBackgroundResource(R.drawable.custombutom2);
                }
                else
                {
                    VFace.setBackgroundResource(R.drawable.custombutom);
                }
            }
        };
        //Chequeo Inicial
        if(AccessToken.getCurrentAccessToken()==null)
        {
            VFace.setBackgroundResource(R.drawable.custombutom2);
        }
        else
        {
            VFace.setBackgroundResource(R.drawable.custombutom);
        }

        /////////////////////////////////////////////////////////Api de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(MenuPrincipalUsuarioProveedor.this.getApplicationContext(), gso);
        signInButton = findViewById(R.id.Vgoogle);
        //signInButton.setSize(SignInButton.SIZE_WIDE);
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MenuPrincipalUsuarioProveedor.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Quiere desvincular su cuenta de google ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                signOut();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //cancelar();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                account = GoogleSignIn.getLastSignedInAccount(MenuPrincipalUsuarioProveedor.this.getApplicationContext());
                if(account != null)
                {
                    dialogo1.show();
                }
                else
                {
                    ApiActivada = 2;
                    signIn();

                }

            }
        });
        account = GoogleSignIn.getLastSignedInAccount(MenuPrincipalUsuarioProveedor.this.getApplicationContext());
        if(account != null)
        {
            VGoo.setBackgroundResource(R.drawable.custombutom);
        }
        else
        {
            VGoo.setBackgroundResource(R.drawable.custombutom2);
        }

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
                dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Quieres salir de la sesion iniciada ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        TokenController.getToken().delete();
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        if(AccessToken.getCurrentAccessToken()!=null)
                        {
                            LoginManager.getInstance().logOut();
                        }
                        if(account != null)
                        {
                            signOut();
                        }
                        Intent intentVuelta = new Intent(MenuPrincipalUsuarioProveedor.this, MainActivity.class);
                        startActivity(intentVuelta);
                        finish();
                    }
                });
                dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //cancelar();
                    }
                });
                dialogo1.show();
                break;
            }
        }
        //drawerLayout.closeDrawer(GravityCompat.START);
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
    private boolean verificarContraseña(String password, String passwordConfirmacion) {
        boolean verificarPassword=false;
        if (password.length()>=8||password.length()>18){
            if (password.matches("[A-Za-z0-9]+") && password.length() > 2){
                if (!password.matches(passwordConfirmacion)){
                    passwordConfirm.setError("Las contraseñas no coinciden");
                    verificarPassword=true;
                }
            }
            else {
                passwordUser.setError("La contraseña solo puede tener letras o numeros");
            }
        }
        else {
            passwordUser.setError("La contraseña debe tener un minimo de 8 caracteres " +
                    "y un maximo de 18");
            verificarPassword=true;
        }

        return  verificarPassword;
    }
    private boolean verificarCumpleaños(int year, int month, int day) {

        int diff = (currentTime.getYear()+1900) - year;
        Log.d(TAG, "Año cumpleaños"+year);
        Log.d(TAG, "Año actual"+(currentTime.getYear()+1900));
        if (currentTime.getMonth() > month ||
                (currentTime.getMonth() == month && currentTime.getDay() >= day)) {
            diff--;
        }
        Log.d(TAG, "Años " + diff);
        if (diff<13){
            mDisplayDate.setError("Tienes que ser mayor a 13 años");
            return true;
        }
        else{
            return false;
        }
    }

    public void animation(View view)
    {
        view.setVisibility(View.GONE);
    }
    public void animation1(View row)
    {
        Animation mLoadAnimation = AnimationUtils.loadAnimation(MenuPrincipalUsuarioProveedor.this.getApplicationContext(), android.R.anim.fade_in);
        row.startAnimation(mLoadAnimation);
        row.setVisibility(View.VISIBLE);
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(MenuPrincipalUsuarioProveedor.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        VGoo.setBackgroundResource(R.drawable.custombutom2);
                        Toast.makeText(MenuPrincipalUsuarioProveedor.this,"Saliste de la sesion " ,Toast.LENGTH_LONG).show();
                        // [END_EXCLUDE]
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    }

