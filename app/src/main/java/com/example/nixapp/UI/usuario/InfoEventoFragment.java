package com.example.nixapp.UI.usuario;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.Interfaces.InfoEventoFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoEventoFragment extends BottomSheetDialogFragment {

    TextView fecha, nombre, hora, lugar;
    Button verMas;

    static InfoEventoFragment instance;

    InfoEventoFragmentListener listener;

    public static InfoEventoFragment getInstance() {
        if(instance==null){
            instance = new InfoEventoFragment();
        }
        return instance;
    }

    public void setListener(InfoEventoFragmentListener listener) {
        this.listener = listener;
    }

    public InfoEventoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_info_evento, container, false);
        fecha = (TextView) itemView.findViewById(R.id.txt_fecha_info_reducida);
        nombre = (TextView) itemView.findViewById(R.id.txt_nombre_info_reducida);
        hora = (TextView) itemView.findViewById(R.id.txt_hora_info_reducida);
        lugar = (TextView) itemView.findViewById(R.id.txt_lugar_info_reducida);
        verMas = (Button) itemView.findViewById(R.id.btn_ver_mas_evento);
        fecha.setText("Fecha: "+getArguments().getString("fecha"));
        nombre.setText(getArguments().getString("nombre"));
        hora.setText("Hora: " + getArguments().getString("hora"));
        lugar.setText(getArguments().getString("lugar"));

        verMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMoreInfoClickListener();
            }
        });
        return itemView;
    }
}
