package com.example.nixapp.UI.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.nixapp.DB.Usuario;
import com.example.nixapp.DB.controllers.TokenController;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.MenuPrincipalUsuarioProveedor;
import com.example.nixapp.UI.usuario.MenuPrincipalUsuarioGeneral;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.modelotablas.UsuarioToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends FragmentActivity{

    /**Crea la vista del activity
     * Primeramente, se define la vista que contendrá el activity.
     * Después se obtendrá el token guardado en la base de datos local, si el token está vacio se
     * llamaá a la función de pantallaInicioSesión
     * En dado caso que el token tenga algún valor. Se enviará a la función principal. Enviando el
     * token a esta función
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        UsuarioToken token = TokenController.getToken();
        if (token == null) {
            pantallaInicioSesion();
        }
        else {
            principal(token);
        }
    }
    /**Llama al activity del Usuario General
     * Se instancian las variables nixClient y nixService, para obtener una conexión con la api
     * mediante la librería de retrofit. Después se realiza el metodo Call, buscando una respuesta
     * del tipo Usuario, en esta llamada se llama a la función getUser del servicio, el cual se
     * espera que nos regrese los datos del usuario, es entonces que se coloca la llamada en un
     * estado en el que espera una llamada de retorno en la cual se mostrará la respuesta del
     * servicio.
     * Después de obtener la respuesta se utiliza una condicionante, en la cual se verificará la
     * respuesta del sitio, en dado caso que la respuesta obtenga un codigo de que el proceso fue
     * satisfactorio se llamará a la activity del usuario general mediante un intent, antes de
     * llamar al activity, se utiliza el comando putExtra, el cual solicita 2 parámetros, el primer
     * parametro que necesita es el nombre con el que se identificará la información extra para el
     * siguiente activity. El segundo parámetro que se coloca es la respuesta del api, esta
     * respuesta es la que contiene los datos del usuario. Al final se llama el activity del usuario
     * general.
     * Para el segundo condicionante se solicita solo si la respuesta que se obtuvo del servicio fue
     * un codigo 401, el cual nos indica que no existe el token en la base de datos y por
     * consiguiente, el usuario no fue encontrado, para después llamar al main activity para que el
     * usuario pueda iniciar sesión o crear una cuenta.
     * El último condicionante es para en dado caso que el servicio retorne un codigo no
     * especificado el cual significa que no le fue posible localizar el API. por lo que se envía un
     * Toast con el mensaje de que el usuario no está conectado a internet
     * Y en dado caso que el servicio no haya enviado respuesta, se realizará lo mismo que en el
     * caso del último condicionante
     *
     * @param usuarioToken
     */
    void principal(UsuarioToken usuarioToken) {

        NixClient nixClient = NixClient.getInstance();
        NixService nixService = nixClient.getNixService();
        Call<Usuario> call = nixService.getUser();
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    if (response.body().tipoUsuario==0) {
                        Intent intent = new Intent(WelcomeActivity.this,
                                MenuPrincipalUsuarioGeneral.class);
                        intent.putExtra("usuario", response.body());
                        startActivity(intent);
                    }
                    else if (response.body().tipoUsuario==1){
                        Intent intent = new Intent(WelcomeActivity.this,
                                MenuPrincipalUsuarioProveedor.class);
                        intent.putExtra("usuario", response.body());
                        startActivity(intent);
                    }
                } else if (response.code() == 401) {

                    pantallaInicioSesion();
                } else {
                    Toast.makeText(WelcomeActivity.this,
                            "Revisa tu conexion a internet.\n Intentalo de nuevo mas tarde",
                            Toast.LENGTH_SHORT).show();
                    Log.i("error",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(WelcomeActivity.this,
                        "Revisa tu conexion a internet.\n Intentalo de nuevo mas tarde",
                        Toast.LENGTH_SHORT).show();
                Log.i("error",t.getMessage());
            }
        });


    }
    /**Llamar main activity
     * Mediante la función start activity, se coloca que desde el welcomeactivity se llamará al
     * main activity, y el metodo finish funciona para que el usuario no pueda volver al este
     * activity
     *
     */
    void pantallaInicioSesion() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }
}
