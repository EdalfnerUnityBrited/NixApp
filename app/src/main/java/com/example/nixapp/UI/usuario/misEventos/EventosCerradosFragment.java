package com.example.nixapp.UI.usuario.misEventos;

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

public class EventosCerradosFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos_cerrados, container, false);

        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.nuevo_evento);
        actionButton.setOnClickListener(this);
        return view;




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nuevo_evento:{
                Intent intent = new Intent(getActivity(), CrearEvento.class);
                startActivity(intent);
                break;
            }
        }
    }
}
