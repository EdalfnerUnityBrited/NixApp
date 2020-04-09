package com.example.nixapp.UI.welcome;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.MenuPrincipalUsuarioGeneral;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.LoginResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioSesion extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText etEmail, etPassword;
    NixService nixService;
    NixClient nixClient;

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

        btnLogin=findViewById(R.id.login);
        etEmail=findViewById(R.id.username);
        etPassword=findViewById(R.id.password);

        btnLogin.setOnClickListener(this);

        retrofitInit();

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
        Call<LoginResult> call = nixService.login(requestSample);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InicioSesion.this, "Sesion Iniciada", Toast.LENGTH_SHORT).show();
                    response.body().procesarRespuesta();
                    Intent i= new Intent(InicioSesion.this, MenuPrincipalUsuarioGeneral.class);
                    i.putExtra("usuario", response.body().usuario);

                    Log.i("Sesion Iniciada",response.body().toString());
                    startActivity(i);
                    finish();
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


}
