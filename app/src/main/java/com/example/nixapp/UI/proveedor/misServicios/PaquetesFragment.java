package com.example.nixapp.UI.proveedor.misServicios;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nixapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PaquetesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paquetes_proveedor,container,false);
        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.nuevo_paquete);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentArticulo = new Intent(getActivity(), CrearPaquetesDatos.class);
                intentArticulo.putExtra("id",CrearServicioMenu.servicio);
                startActivity(intentArticulo);
            }
        });

        return view;
    }
}
