package com.example.nixapp.UI.welcome;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.MenuPrincipalUsuarioProveedor;
import com.example.nixapp.UI.usuario.MenuPrincipalUsuarioGeneral;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.LoginResult;
import com.example.nixapp.conn.results.UsuarioResult;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioSesion extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog mProgressDialog;
    //////////////////
    private ProfileTracker profileTracker;
    CallbackManager callbackManager;
    LoginButton loginButton;
    TextView ids, emails;
    AccessTokenTracker accessTokenTracker;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account ;
    SignInButtonImpl signInButton;
    Button fake;
    int ApiActivada = 0;
    int RC_SIGN_IN = 9001;
    boolean imagen_agregada = false;
    Uri imagen_enviar = null;
    //////////////////
    Button btnLogin,crearcuenta;
    EditText etEmail, etPassword;
    NixService nixService;
    NixClient nixClient;
    String emailF ="",edadF = "", idF = "",nameF = "",nameCF ="", middleF = "", lastnF= "", nombreG = "",apellidosG = "", emailG = "" , givennameG = "";
    String FotoPerfilF,FotoPerfilG;
    Usuario nuevo;
    String editTextInput;
    String intentoContra;
    EditText editTextField,editTextField2;
    AlertDialog.Builder dialogo1,dialog,dialogFace;
    /**Inicializar Variables
     * Se coloca el respectivo layout al activity mediante la función setContentView
     * Se referencian las variables con sus respectivos elementos en la vista
     * Al btnLogin se le coloca la propiedad para el caso que sea presionado
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        mProgressDialog= new ProgressDialog(this);
        btnLogin=findViewById(R.id.login);
        etEmail=findViewById(R.id.username);
        etPassword=findViewById(R.id.password);
        crearcuenta = findViewById(R.id.crear_cuenta);
        btnLogin.setOnClickListener(this);
        retrofitInit();

        /////////////////////////////Inicializacion de un dialog
        dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Quiere salir de la sesion de google iniciada ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                signOut();
            }
        });
        dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //cancelar();
            }
        });

        ////////////////////////////////////////////////////////////
        crearcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentBusqueda = new Intent(getApplicationContext(), CrearCuenta.class);
                startActivity(intentBusqueda);
            }
        });

        //////////////////////////////////////////////////////////Facebook API
        callbackManager = CallbackManager.Factory.create();
        //Boton falso "Invisible"
        loginButton = (LoginButton) findViewById(R.id.login_facebook);
        loginButton.setVisibility(View.INVISIBLE);
        fake = findViewById(R.id.buttonFacebook);
        fake.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginButton.performClick();

            }
        });
        ///////////////////////////////////////////////////Fin del boton falso
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("user_birthday");
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        // Callback registration

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged (Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    displayProfileInfo(currentProfile);
                }
            }
        };
        loginButton.registerCallback(callbackManager, new FacebookCallback<com.facebook.login.LoginResult>() {
            @Override
            public void onSuccess(com.facebook.login.LoginResult loginResult) {
                // App code
                if(AccessToken.getCurrentAccessToken()==null)
                {
                    ids.setText("Porfavor inicia sesion... denuevo");
                }
                else
                {
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {

                        displayProfileInfo(profile);
                        requestEmail(AccessToken.getCurrentAccessToken());
                        requestEdad(AccessToken.getCurrentAccessToken());
                        Toast.makeText(getApplicationContext(),"Iniciaste Sesion con facebook",Toast.LENGTH_SHORT).show();


                    } else {
                        Profile.fetchProfileForCurrentAccessToken();
                        requestEmail(AccessToken.getCurrentAccessToken());
                        requestEdad(AccessToken.getCurrentAccessToken());
                        Toast.makeText(getApplicationContext(),"Wow, si iniciaste con facebook",Toast.LENGTH_SHORT).show();
                        

                    }
                }

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(),"Intento Cancelado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(),"Error al intentar iniciar sesion",Toast.LENGTH_SHORT).show();
            }
        });

        //PA salir de la sincronizacion de facebook LoginManager.getInstance().logOut();
        //AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        ////////////////////////////////////////// Inicio de APi de Google

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton = findViewById(R.id.googleInicio);
        // Dimensiones del boton de inicio de sesion.
        //signInButton.setSize(SignInButton.SIZE_WIDE);

        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
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

        ///////////////////////////////////////////////////////////////
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Importante");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Iniciar Sesion", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    intentoContra = editTextField.getText().toString();
                final Usuario requestSample = new Usuario(emailG, intentoContra);
                mProgressDialog.setTitle("Iniciando Sesion...");
                mProgressDialog.setMessage("Por favor espere");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<LoginResult> call = nixService.login(requestSample);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InicioSesion.this, "Sesion Iniciada", Toast.LENGTH_SHORT).show();
                            response.body().procesarRespuesta();
                            Usuario usuario= response.body().usuario;
                            if (usuario.tipoUsuario==0){
                                Intent i= new Intent(InicioSesion.this, MenuPrincipalUsuarioGeneral.class);
                                i.putExtra("usuario", response.body().usuario);

                                Log.i("Sesion Iniciada",response.body().toString());
                                mProgressDialog.dismiss();
                                startActivity(i);
                                finish();
                            }
                            else if(usuario.tipoUsuario==1) {
                                Intent i= new Intent(InicioSesion.this, MenuPrincipalUsuarioProveedor.class);
                                i.putExtra("usuario", response.body().usuario);

                                Log.i("Sesion Iniciada",response.body().toString());
                                mProgressDialog.dismiss();
                                startActivity(i);
                                finish();
                            }
                        } else {
                            try {
                                Log.i("Error",response.errorBody().string().toString());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            mProgressDialog.dismiss();
                            Toast.makeText(InicioSesion.this, "Error en la contraseña", Toast.LENGTH_SHORT).show();
                            signOut();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(InicioSesion.this, "Error en la llamada", Toast.LENGTH_SHORT).show();
                    }
                });
                mProgressDialog.dismiss();

            }
        });
        dialog.setNegativeButton("No,gracias", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                    signOut();
            }
        });


        //////////////////////////////////////////////////////////////
        editTextField2 = new EditText(this);
        dialogFace = new AlertDialog.Builder(this);
        dialogFace.setTitle("Importante");
        dialogFace.setView(editTextField2);
        dialogFace.setCancelable(false);
        dialogFace.setPositiveButton("Iniciar Sesion", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogFace, int id) {
                intentoContra = editTextField2.getText().toString();
                final Usuario requestSample = new Usuario(emailF, intentoContra);
                mProgressDialog.setTitle("Iniciando sesion...");
                mProgressDialog.setMessage("Por favor espere");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<LoginResult> call = nixService.login(requestSample);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InicioSesion.this, "Sesion Iniciada", Toast.LENGTH_SHORT).show();
                            response.body().procesarRespuesta();
                            Usuario usuario= response.body().usuario;
                            if (usuario.tipoUsuario==0){
                                Intent i= new Intent(InicioSesion.this, MenuPrincipalUsuarioGeneral.class);
                                i.putExtra("usuario", response.body().usuario);

                                Log.i("Sesion Iniciada",response.body().toString());
                                mProgressDialog.dismiss();
                                startActivity(i);
                                finish();
                            }
                            else if(usuario.tipoUsuario==1) {
                                Intent i= new Intent(InicioSesion.this, MenuPrincipalUsuarioProveedor.class);
                                i.putExtra("usuario", response.body().usuario);

                                Log.i("Sesion Iniciada",response.body().toString());
                                mProgressDialog.dismiss();
                                startActivity(i);
                                finish();
                            }
                        } else {
                            try {
                                Log.i("Error",response.errorBody().string().toString());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            Toast.makeText(InicioSesion.this, "Error en la contraseña", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                            LoginManager.getInstance().logOut();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(InicioSesion.this, "Error en la llamada", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                });
            }
        });
        dialogFace.setNegativeButton("No,gracias", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogFace, int id) {

                LoginManager.getInstance().logOut();
            }
        });

    }

    /**Se inicia el servicio
     * Al igual que en el WelcomeActivity se obtiene la referencia para establecer conexión con la
     * API
     *
     */
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    /**Se obtienen los valores de los EditText
     * primeramente se obtienen los valores de los editText y se almacenan en las variables
     * respectivas.
     * Se pasa a las condicionantes, en las cuales se corrobora que los campos no estén vacíos, en
     * dado caso que alguno de los campos se encuentre vacio no se procederá a la función de
     * verificar el login y se colocarán mensajes de error donde hace falta que se llenen los
     * campos
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String email = etEmail.getText().toString();
        String password= etPassword.getText().toString();
        if (email.isEmpty()&&password.isEmpty()){
            etEmail.setError("Ingrese Email");
            etPassword.setError("Ingrese Contraseña");
        }
        if (email.isEmpty()){
            etEmail.setError("Ingrese Email");
        }
        if (password.isEmpty()){
            etPassword.setError("Ingrese Password");
        }
        else{
            goToLogin();
        }

    }

    /**Verificar datos
     * Se obtienen los datos de los edit text y se almacenan en sus variables corresponientes
     * Después se crea una variable de tipo Usuario, en los cuales se envia el correo y la
     * contraseña para poder crear el objeto, despues se realiza la llamada, utilizando el metodo
     * login de la interfaz del servicio, en la cual se envia el objeto creado.
     * Se realiza la llamada y se espera para una respuesta, en dado caso que no se encuentre
     * respuesta no se realizará nada, en caso que se encuentre respuesta se pasará a un
     * condicionante en el cual verificará si la respuesta de la api fue positiva, en ese caso como
     * en el welcomeactivity se pasara a la pantalla del usuario general con los extras de los datos
     * del usuario además se llamará a la función procesar respuesta, en la cual se guardrá el token
     * para que se quede en la base de datos local.
     * En dado caso que la respuesta no sea un codigo satisfactorio, se llamara al otro
     * condicionante en el cual se colocará un toast mostrando que hubo un error en los datos
     *
     */
    private void goToLogin() {
        String email= etEmail.getText().toString();
        String password= etPassword.getText().toString();
        final Usuario requestSample = new Usuario(email, password);
        mProgressDialog.setTitle("Iniciando sesion...");
        mProgressDialog.setMessage("Por favor espere");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        Call<LoginResult> call = nixService.login(requestSample);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InicioSesion.this, "Sesion Iniciada", Toast.LENGTH_SHORT).show();
                    response.body().procesarRespuesta();
                    Usuario usuario= response.body().usuario;
                    if (usuario.tipoUsuario==0){
                    Intent i= new Intent(InicioSesion.this, MenuPrincipalUsuarioGeneral.class);
                    i.putExtra("usuario", response.body().usuario);

                    Log.i("Sesion Iniciada",response.body().toString());
                    mProgressDialog.dismiss();
                    startActivity(i);
                    finish();
                    }
                    else if(usuario.tipoUsuario==1) {
                        Intent i= new Intent(InicioSesion.this, MenuPrincipalUsuarioProveedor.class);
                        i.putExtra("usuario", response.body().usuario);

                        Log.i("Sesion Iniciada",response.body().toString());
                        mProgressDialog.dismiss();
                        startActivity(i);
                        finish();
                    }
                } else {
                    try {
                        Log.i("Error",response.errorBody().string().toString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Toast.makeText(InicioSesion.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    ///////////////////////// Funciones de API google

    // Tomar informacion del perfil de google
    public void getinformation()
    {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            nombreG = acct.getDisplayName();
            givennameG = acct.getGivenName();
            apellidosG = acct.getFamilyName();
            emailG = acct.getEmail();
            String personId = acct.getId();
            try {
                FotoPerfilG = acct.getPhotoUrl().toString();
            }
            catch (Exception e)
            {
                FotoPerfilG = "";
            }


            nuevo = new Usuario(emailG);
            Call<UsuarioResult> call = nixService.verificacionEmail(nuevo);
            call.enqueue(new Callback<UsuarioResult>() {
                @Override
                public void onResponse(Call<UsuarioResult> call, Response<UsuarioResult> response) {
                    if(response.isSuccessful())
                    {
                        Usuario chequeo = response.body().usuario;
                        if(chequeo == null)
                        {
                            Intent i= new Intent(InicioSesion.this,CrearCuenta.class);
                            i.putExtra("nombreU", givennameG);
                            i.putExtra("segundoNombreU", "");
                            i.putExtra("apellidoU", apellidosG);
                            i.putExtra("emailU", emailG);
                            i.putExtra("edadU", "");
                            i.putExtra("fotoPerfil", FotoPerfilG);
                            startActivity(i);
                            finish();
                            Log.i("Crear Google fallido",response.body().toString());
                            signOut();

                        }
                        else
                        {

                            Toast.makeText(getApplicationContext(),"Ya tienes cuenta de Google",Toast.LENGTH_SHORT).show();
                            editTextField = new EditText(InicioSesion.this);
                            dialog.setView(editTextField);
                            dialog.setMessage("Deseas iniciar sesion con la cuenta registrada con el correo: " + emailG+"?");
                            dialog.show();



                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error: " + response.errorBody().toString(),Toast.LENGTH_SHORT).show();
                        Log.i("Error sistema", response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<UsuarioResult> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error: No se encontro la ruta", Toast.LENGTH_SHORT).show();
                }
            });
            //Glide.with(this).load(personPhoto) .diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(fotoperfil); Ponerle la imagen a una view
        }
    }

    //Validacion de la cuenta y de la activacion de API en servers de google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account); //mensaje de Inicio Exitoso
            getinformation(); // Tomar la informacion de perfil de google

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this,"Inicio Interrumpido" + e,Toast.LENGTH_LONG).show();
        }
    }

    //Cerrar sesion de la cuenta de Google
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        //Toast.makeText(getApplicationContext(),"Saliste de la sesion" ,Toast.LENGTH_LONG).show();
                        // [END_EXCLUDE]
                    }
                });
    }


    //Mensajes de estado acerca de si se conecto o no con google
    public void  updateUI(GoogleSignInAccount account){
        if(account != null){
            Toast.makeText(this,"Inicio de sesion exitosa",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"Faild",Toast.LENGTH_LONG).show();
        }
    }
    //Inicio de sesion con Cuenta de Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    ////////////////////////////////////Funciones para Api de Facebook
    //Peticion del email en la cuenta de Facebook
    private void requestEmail(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(getApplicationContext(), response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();//Error en el intento de peticion
                    return;
                }
                try {
                    emailF = object.getString("email"); //Peticion de la informacion en el campo de email
                    setEmail(emailF);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    emails.setText("No se pudo");
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");//Parametros por ejecutar de Json de la informacion
        request.setParameters(parameters);
        request.executeAsync();//Se ejecuta la peticion


    }
    //Peticion de la edad en la cuenta de Facebook
    private void requestEdad(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(getApplicationContext(), response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();//Error en el intento de peticion
                    return;
                }
                try {
                    edadF = object.getString("birthday");//Peticion de la informacion en el campo birthday
                    setEdad(edadF);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");//Parametros por ejecutar de Json de la informacion
        request.setParameters(parameters);
        request.executeAsync();//Se ejecuta la peticion

    }

    //Escritura del TextView Email
    private void setEmail(String email) {
        //Aqui esta el Email del usuario de facebook "email"
        emailF = email;
        nuevo = new Usuario(emailF);
        Call<UsuarioResult> call = nixService.verificacionEmail(nuevo);
        call.enqueue(new Callback<UsuarioResult>() {
            @Override
            public void onResponse(Call<UsuarioResult> call, Response<UsuarioResult> response) {
                if(response.isSuccessful())
                {
                    Usuario chequeo = response.body().usuario;
                    if(chequeo == null)
                    {
                        Intent i= new Intent(InicioSesion.this,CrearCuenta.class);
                        i.putExtra("nombreU", nameF);
                        i.putExtra("segundoNombreU", middleF);
                        i.putExtra("apellidoU", lastnF);
                        i.putExtra("emailU", emailF);
                        i.putExtra("edadU", edadF);
                        i.putExtra("fotoPerfil", FotoPerfilF);
                        Log.i("Crear Facebook fallido",response.body().toString());
                        startActivity(i);
                        finish();
                        LoginManager.getInstance().logOut();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Ya tienes cuenta de Facebook",Toast.LENGTH_SHORT).show();
                        editTextField2 = new EditText(InicioSesion.this);
                        dialogFace.setView(editTextField2);
                        dialogFace.setMessage("Deseas iniciar sesion con la cuenta registrada con el correo: " + emailF+"?");
                        dialogFace.show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error: " + response.errorBody().toString(),Toast.LENGTH_SHORT).show();
                    Log.i("Error sistema", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<UsuarioResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: No se encontro la ruta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Escritura del TextView edad
    private void setEdad(String edad) {
        //Aqui esta la fecha de nacimiento del perfil de facebook "edad"
    }

    //Toma de la informacion basica del perfil de facebook y mostrada
    public void displayProfileInfo(Profile profile)
    {
        idF = profile.getId();
        nameF = profile.getFirstName();
        nameCF = profile.getName();
        middleF = profile.getMiddleName();
        lastnF = profile.getLastName();

        int dimensionPixelSize = getResources().getDimensionPixelSize(com.facebook.R.dimen.com_facebook_profilepictureview_preset_size_large);
        FotoPerfilF = profile.getProfilePictureUri(180,180).toString();
        //Glide.with(this).load(profilePictureUri) .diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(Vista_Donde_Ira_Foto);
    }

    //Destructor del profile cuando existe un "error"
    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    ////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(ApiActivada == 0){ // Facebook API
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
        else if (ApiActivada == 2) // Google API
        {

            //Mensaje
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                ApiActivada = 0;
            }
            ApiActivada = 0;
        }
        else //Demas
        {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imagen_enviar = imageUri;
                    imagen_agregada = true;
                    Toast.makeText(this, "Imagen Agregada: ", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al agregar imagen", Toast.LENGTH_SHORT).show();
                }
            }
            ApiActivada = 0;
        }
    }

}
