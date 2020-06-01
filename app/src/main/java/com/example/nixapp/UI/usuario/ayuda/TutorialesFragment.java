package com.example.nixapp.UI.usuario.ayuda;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.ayuda.Video_Individual;

public class TutorialesFragment extends Fragment {
    Button video1,video2,video3,video4,video5,video6,video7,video8;
    public static String PACKAGE_NAME;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view= inflater.inflate(R.layout.fragment_tutoriales,container,false);
        video1 = view.findViewById(R.id.video1);
        video2 = view.findViewById(R.id.video2);
        video3 = view.findViewById(R.id.video3);
        video4 = view.findViewById(R.id.video4);
        video5 = view.findViewById(R.id.video5);
        video7 = view.findViewById(R.id.video7);
        video8 = view.findViewById(R.id.video8);
        video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.crearevento;
                Intent intent= new Intent(getActivity(), Video_Individual.class);
                intent.putExtra("VideoLiga", path);
                intent.putExtra("Titulo", "Creacion de Eventos");
                startActivity(intent);
            }
        });

        video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.calendario;
                Intent intent= new Intent(getActivity(), Video_Individual.class);
                intent.putExtra("VideoLiga", path);
                intent.putExtra("Titulo", "Uso del Calendario");
                startActivity(intent);
            }
        });

        video3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.confirmarasistencia;
                Intent intent= new Intent(getActivity(), Video_Individual.class);
                intent.putExtra("VideoLiga", path);
                intent.putExtra("Titulo", "Confirmacion de Asistencia");
                startActivity(intent);
            }
        });

        video4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.creadorinvitaciones1;
                Intent intent= new Intent(getActivity(), Video_Individual.class);
                intent.putExtra("VideoLiga", path);
                intent.putExtra("Titulo", "Uso del creador de Invitaciones Parte 1");
                startActivity(intent);
            }
        });
        video5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.creadorinvitaciones2;
                Intent intent= new Intent(getActivity(), Video_Individual.class);
                intent.putExtra("VideoLiga", path);
                intent.putExtra("Titulo", "Uso del creador de Invitaciones Parte 2");
                startActivity(intent);
            }
        });
        video7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.contratacionesusuario;
                Intent intent= new Intent(getActivity(), Video_Individual.class);
                intent.putExtra("VideoLiga", path);
                intent.putExtra("Titulo", "Contratacion de Servicios");
                startActivity(intent);
            }
        });
        video8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.cotizaciones;
                Intent intent= new Intent(getActivity(), Video_Individual.class);
                intent.putExtra("VideoLiga", path);
                intent.putExtra("Titulo", "ABC de las Cotizaciones");
                startActivity(intent);
            }
        });

        return view;
    }
}
