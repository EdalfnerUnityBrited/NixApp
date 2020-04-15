package com.example.nixapp.UI.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nixapp.R;

public class InfoEventoExpandida extends AppCompatActivity {

    private String nombre, fecha, hora, lugar, descripcion, fotoPrincipal;
    private int privacidad, categoria, cupo, cover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_evento_expandida);

        nombre = (String) getIntent().getSerializableExtra("nombre");
        fecha = (String) getIntent().getSerializableExtra("fecha");
        hora = (String) getIntent().getSerializableExtra("hora");
        lugar = (String) getIntent().getSerializableExtra("lugar");
        descripcion = (String) getIntent().getSerializableExtra("descripcion");
        fotoPrincipal = (String) getIntent().getSerializableExtra("fotoPrincipal");

        privacidad = Integer.parseInt((String) getIntent().getSerializableExtra("privacidad"));
        categoria = Integer.parseInt((String) getIntent().getSerializableExtra("categoria"));
        cupo = Integer.parseInt((String) getIntent().getSerializableExtra("cupo"));
        cover = Integer.parseInt((String) getIntent().getSerializableExtra("cover"));

    }
}
