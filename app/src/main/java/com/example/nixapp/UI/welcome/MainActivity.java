package com.example.nixapp.UI.welcome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.UsuarioResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnLogin, btnSignup, password;

    AlertDialog.Builder dialogo1;
    NixService nixService;
    NixClient nixClient;
    long mLastClickTime = 0;
    /**Declaración de variables
     * Primeramente se relacionan los botones con su respectivo elemento en la vista utilizando la
     * función findViewbyId, lo que hace esta función es asignar a la variable la referencia del
     * elemento en el xml
     * Después se les asigna a btnLogin y a btnSignup los metodos para el caso en que sean
     * presionados, esto se realiza mediante la función setOnClickListener(this) en el que this es
     * el contexto en el cual se encuentran
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofitInit();

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnSignup = (Button) findViewById(R.id.buttonSignup);
        password=(Button) findViewById(R.id.passwordFor);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        password.setOnClickListener(this);



    }

    /**Click de botones
     * Se obtiene el id del elemento del boton que se presionó, después se declara el Intent q, el
     * cual nos va a referenciar la siguiente activity.
     * Ahora mediante un switch se verifica cual es el botón que se ha presionado mediante casos con
     * los respectivos id's.
     * En el primer caso se esta verificando el buttonlogin, el cual referenciará el intent para
     * dirigir al usuario al activity de inicio se sesión, y se procederá a iniciar el activity
     * En el segundo caso se está verificando el buttonSignup, el cual referenciará el intent para
     * dirigir al usuario al activity de crear cuenta y se procederá a iniciar el activity
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        final int i = view.getId();
        Intent q;
        if (SystemClock.elapsedRealtime() - mLastClickTime > 1000){
            switch (i) {
                case R.id.buttonLogin: {
                    q = new Intent(view.getContext(), InicioSesion.class);
                    startActivity(q);
                    break;
                }
                case R.id.buttonSignup: {
                    q = new Intent(view.getContext(), CrearCuenta.class);
                    startActivity(q);
                    break;
                }
                case R.id.passwordFor:{
                    dialogo1 = new AlertDialog.Builder(this);
                    dialogo1.setTitle("Recuperacion de Contraseña");
                    dialogo1.setMessage("Ingrese el correo del cual necesitas la reposicion de tu contraseña:");
                    final EditText input = new EditText(MainActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    dialogo1.setView(input);
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            String correo= input.getText().toString();
                            Usuario usuario= new Usuario(correo);
                            Call<UsuarioResult> call= nixService.verificacionEmail(usuario);
                            call.enqueue(new Callback<UsuarioResult>() {
                                @Override
                                public void onResponse(Call<UsuarioResult> call, Response<UsuarioResult> response) {
                                    if (response.isSuccessful()){
                                        Usuario revisar = response.body().usuario;
                                        if (revisar==null){
                                            Toast.makeText(MainActivity.this, "Ese correo no está registrado", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(MainActivity.this, RecuperarContra.class);
                                            i.putExtra("usuario", revisar);
                                            startActivity(i);
                                        }
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UsuarioResult> call, Throwable t) {

                                }
                            });
                        }
                    });
                    dialogo1.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //cancelar();
                        }
                    });
                    dialogo1.show();
                    break;
                }
            }
        }
        mLastClickTime = SystemClock.elapsedRealtime();

    }

    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
