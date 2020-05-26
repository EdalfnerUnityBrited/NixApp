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
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.GenerateQRListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.SetImageSizeListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class QrCodeFragment extends BottomSheetDialogFragment implements GenerateQRListener {

    Button confirmacion;
    EditText enlace;

    GenerateQRListener listener;

    public void setListener(GenerateQRListener listener) {
        this.listener = listener;
    }

    static QrCodeFragment instance;

    public static QrCodeFragment getInstance() {
        if (instance==null){
            instance=new QrCodeFragment();
        }
        return instance;
    }

    public QrCodeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView =  inflater.inflate(R.layout.fragment_qr, container, false);

        confirmacion = (Button)itemView.findViewById(R.id.btn_generate_qr);
        enlace = (EditText)itemView.findViewById(R.id.edtTxt_enlace);

        confirmacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.generateCodeQr(enlace.getText().toString());
                dismiss();
            }
        });

        return itemView;
    }


    @Override
    public void generateCodeQr(String enlace) {
        enlace = this.enlace.getText().toString();
    }
}
