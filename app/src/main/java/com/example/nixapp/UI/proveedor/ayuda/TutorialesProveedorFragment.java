package com.example.nixapp.UI.proveedor.ayuda;

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

public class TutorialesProveedorFragment extends Fragment {

    Button video1,video2,video3,video4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutoriales_proveedor, container, false);
        video1 = view.findViewById(R.id.video1);
        video2 = view.findViewById(R.id.video1);
        video3 = view.findViewById(R.id.video1);
        video4 = view.findViewById(R.id.video1);

        video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.crearevento;
                Intent intent= new Intent(getActivity(), Video_Individual.class);
                intent.putExtra("VideoLiga", path);
                intent.putExtra("Titulo", "Creacion de Servicio");
                startActivity(intent);
            }
        });


        return view;
    }
}
