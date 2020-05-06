package com.example.nixapp.UI.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnLogin, btnSignup;
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

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnSignup = (Button) findViewById(R.id.buttonSignup);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

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
        int i = view.getId();
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
            }
        }
        mLastClickTime = SystemClock.elapsedRealtime();

    }


}
