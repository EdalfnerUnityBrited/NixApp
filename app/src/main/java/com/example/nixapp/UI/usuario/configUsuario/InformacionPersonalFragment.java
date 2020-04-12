package com.example.nixapp.UI.usuario.configUsuario;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class InformacionPersonalFragment extends Fragment {

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
    private static final int GALLERY_INTENT=1;
    private StorageReference mStorage;
    private static final String TAG = "MainActivity";
    private TextView mDisplayDate=null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int ano, mes, dia;
    private CircleImageView fotoPerfil;
    private ProgressDialog mProgressDialog;
    NixService nixService;
    NixClient nixClient;
    Date currentTime = Calendar.getInstance().getTime();
    String date, downloadUrl;
    boolean cambio = true,cambio2 = true;
    Usuario usuario;
    EditText nombre, telefono, apP, apM, correo, passwordUser, passwordConfirm;
    TextView fecha, saludo;
    Button cambioDato, passwordCambio;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        View view;
        view = inflater.inflate(R.layout.fragment_informacion_personal, container, false);
        retrofitInit();
        usuario= (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        mStorage= FirebaseStorage.getInstance().getReference().child("Fotos");
        mProgressDialog= new ProgressDialog(getActivity());
        nombre= view.findViewById(R.id.nombre_user);
        nombre.setText(usuario.name);
        telefono= view.findViewById(R.id.telefono_usuario);
        telefono.setText(usuario.telefono);
        fecha= view.findViewById(R.id.fecha_nacimiento);
        fecha.setText(usuario.fechaNac);
        correo=view.findViewById(R.id.email_user);
        correo.setText(usuario.email);
        apP = view.findViewById(R.id.ap_pat_user);
        apM = view.findViewById(R.id.ap_mat_user);
        cambioDato=view.findViewById(R.id.buttonGuardar);
        saludo=view.findViewById(R.id.nombre_inicial);
        saludo.setText("Hola, "+usuario.name);
        passwordUser= view.findViewById(R.id.password_usuario);
        passwordConfirm= view.findViewById(R.id.confirmacion_contra);
        passwordCambio= view.findViewById(R.id.buttonPass);
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
                                Toast.makeText(getActivity(), "Actualización Exitosa", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();

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
                String nombreUsuario = nombre.getText().toString();
                String telefonoUsuario = telefono.getText().toString();
                String ap = apP.getText().toString();
                String am = apM.getText().toString();
            if (nombreUsuario.isEmpty()){
                nombre.setError("Llene el nombre");
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
                                Toast.makeText(getActivity(), "Actualización Exitosa", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();

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
        fotoPerfil= view.findViewById(R.id.profile_image);
        Glide.with(fotoPerfil)
                .load(usuario.fotoPerfil)
                .fitCenter()
                .centerCrop()
                .into(fotoPerfil);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);

            }
        });

        mDisplayDate = (TextView) view.findViewById(R.id.fecha_nacimiento);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
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

        final EditText pass = view.findViewById(R.id.password_usuario);
        final EditText pass_conf = view.findViewById(R.id.confirmacion_contra);
        final View row = view.findViewById(R.id.confirmacion);
        final View view2 = view.findViewById(R.id.view2);
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
        final EditText nombre = view.findViewById(R.id.nombre_user);
        final View rowapP = view.findViewById(R.id.apellido_paterno);
        final View rowapM = view.findViewById(R.id.apellido_materno);
        final View invi = view.findViewById(R.id.inv1);
        final View invi2 = view.findViewById(R.id.invi2);

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
        loginButton = (LoginButton) view.findViewById(R.id.faceprueba);
        loginButton.setVisibility(View.INVISIBLE);
        loginButton.setFragment(this);
        fake = view.findViewById(R.id.Vface);
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
                Toast.makeText(getActivity(),"Vinculacion con facebook exitosa",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity(),"Intento Cancelado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getActivity(),"Error al intentar iniciar sesion",Toast.LENGTH_SHORT).show();
            }
        });
        final Button VFace = view.findViewById(R.id.Vface);
        VGoo = view.findViewById(R.id.Vgoogle);
        //PA salir de la sincronizacion de facebook LoginManager.getInstance().logOut();
        //AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user clicks on facebook logout
                    Toast.makeText(getActivity(),"Cuenta Desvinculada",Toast.LENGTH_SHORT).show();
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
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity().getApplicationContext(), gso);
        signInButton = view.findViewById(R.id.Vgoogle);
        //signInButton.setSize(SignInButton.SIZE_WIDE);
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
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
                account = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
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
        return view;
    }

/////////////////////////////////////////////API Facebook
//Destructor del profile cuando existe un "error"
@Override
public void onDestroy() {
    super.onDestroy();
    profileTracker.stopTracking();
}

/////////////////////////////////////////////API Google
//Validacion de la cuenta y de la activacion de API en servers de google
private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
        GoogleSignInAccount account = completedTask.getResult(ApiException.class);

        // Signed in successfully, show authenticated UI.
        Toast.makeText(getActivity(),"Vinculacion exitosa",Toast.LENGTH_LONG).show();
        VGoo.setBackgroundResource(R.drawable.custombutom);

    } catch (ApiException e) {
        // The ApiException status code indicates the detailed failure reason.
        // Please refer to the GoogleSignInStatusCodes class reference for more information.
        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        Toast.makeText(getActivity(),"Inicio Interrumpido",Toast.LENGTH_LONG).show();
    }
}

    //Cerrar sesion de la cuenta de Google
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        VGoo.setBackgroundResource(R.drawable.custombutom2);
                        Toast.makeText(getActivity(),"Saliste de la sesion " ,Toast.LENGTH_LONG).show();
                        // [END_EXCLUDE]
                    }
                });
    }


    //Mensajes de estado acerca de si se conecto o no con google
    public void  updateUI(GoogleSignInAccount account){
        if(account != null){

        }else {
            Toast.makeText(getActivity(),"Faild",Toast.LENGTH_LONG).show();
        }
    }
    //Inicio de sesion con Cuenta de Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ApiActivada == 0){ // Facebook API
            callbackManager.onActivityResult(requestCode, resultCode, data);
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
                        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Subida Exitosa", Toast.LENGTH_SHORT).show();
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
                                        .fitCenter()
                                        .centerCrop()
                                        .into(fotoPerfil);
                                final Usuario requestSample = new Usuario(downloadUrl);
                                Call<ResponseBody> call = nixService.foto(requestSample);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Cambio de foto correcto",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Error en los datos",
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
    }

    public void animation(View view)
    {
        view.setVisibility(View.GONE);
    }
    public void animation1(View row)
    {
        Animation mLoadAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);
        row.startAnimation(mLoadAnimation);
        row.setVisibility(View.VISIBLE);
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
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
}
