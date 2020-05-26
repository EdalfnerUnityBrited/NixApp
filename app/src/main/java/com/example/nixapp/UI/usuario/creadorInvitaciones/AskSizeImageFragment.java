package com.example.nixapp.UI.usuario.creadorInvitaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.SetImageSizeListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AskSizeImageFragment extends BottomSheetDialogFragment implements SetImageSizeListener{

    Button setSize;
    EditText alto,ancho;

    SetImageSizeListener listener;

    public void setListener(SetImageSizeListener listener) {
        this.listener = listener;
    }

    static AskSizeImageFragment instance;

    public static AskSizeImageFragment getInstance() {
        if (instance == null){
            instance = new AskSizeImageFragment();
        }
        return instance;
    }

    public AskSizeImageFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView =  inflater.inflate(R.layout.fragment_image_size, container, false);

        setSize = (Button)itemView.findViewById(R.id.btn_set_image_size);
        alto = (EditText)itemView.findViewById(R.id.edtTxt_alto_imagen);
        ancho = (EditText)itemView.findViewById(R.id.edtTxt_ancho_imagen);

        setSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    listener.setImageSize(Integer.parseInt(alto.getText().toString()), Integer.parseInt(ancho.getText().toString()));
                    dismiss();
                }
                catch (Exception e){
                    listener.setImageSize(250,250);
                    dismiss();
                }
            }
        });
        return itemView;
    }

    @Override
    public void setImageSize(int alto, int ancho){
        alto = Integer.parseInt(this.alto.getText().toString());
        ancho = Integer.parseInt(this.ancho.getText().toString());
    }
}
