package com.example.nixapp.UI.usuario.Tendencias;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;

public class Tendencias extends AppCompatActivity implements EventosTendenciasFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tendencias);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle("Tendencias");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onListFragmentInteraction(Eventos item) {
        Toast.makeText(this, "Me seleccionaste", Toast.LENGTH_SHORT).show();
    }
    public void setToolbarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }
}
