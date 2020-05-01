package com.example.nixapp.UI.usuario.Tendencias;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.EventosCerradosFragment;

public class Tendencias extends AppCompatActivity implements EventosTendenciasFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tendencias);
    }

    public void onListFragmentInteraction(Eventos item) {
        Toast.makeText(this, "Me seleccionaste", Toast.LENGTH_SHORT).show();
    }
}
