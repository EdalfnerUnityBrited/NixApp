package com.example.nixapp.UI;
import com.example.nixapp.DB.Conexion;
import com.example.nixapp.R;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InicioSesion extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView tvGoSignUp;
    EditText etEmail, etPassword;
    Intent intent;

    private Conexion conne= new Conexion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        btnLogin=findViewById(R.id.login);
        etEmail=findViewById(R.id.username);
        etPassword=findViewById(R.id.password);

        btnLogin.setOnClickListener(this);

        conne.connectionBD();
    }

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
            boolean usuarioEncontrado=conne.validarUsuarios(email, password);
            if (usuarioEncontrado==true){
                Toast.makeText(getApplicationContext(),"Bienvenido",Toast.LENGTH_LONG).show();
                Intent intentMenuPrincipal = new Intent(getApplicationContext(),MenuPrincipalUsuarioGeneral.class);
                startActivity(intentMenuPrincipal);
                finish();
            }else {
                Toast.makeText(getApplicationContext(),"Vete",Toast.LENGTH_LONG).show();
            }
        }

    }
}
