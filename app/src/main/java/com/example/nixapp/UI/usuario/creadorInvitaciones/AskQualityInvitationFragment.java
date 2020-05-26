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
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.SetInvitationQualityListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AskQualityInvitationFragment extends BottomSheetDialogFragment implements SetInvitationQualityListener {

    Button confirmacion;
    EditText calidad;

    SetInvitationQualityListener listener;

    public void setListener(SetInvitationQualityListener listener) {
        this.listener = listener;
    }

    static AskQualityInvitationFragment instance;

    public static AskQualityInvitationFragment getInstance() {
        if (instance==null){
            instance = new AskQualityInvitationFragment();
        }
        return instance;
    }

    public AskQualityInvitationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_quality_invitation, container, false);

        confirmacion = (Button)itemView.findViewById(R.id.btn_set_quality);
        calidad = (EditText)itemView.findViewById(R.id.edtTxt_calidad);

        confirmacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    listener.setImageQuality(Integer.parseInt(calidad.getText().toString()));
                    dismiss();
                }
                catch (Exception e){
                    listener.setImageQuality(100);
                    dismiss();
                }
            }
        });

        return itemView;
    }

    @Override
    public void setImageQuality(int quality) {
        quality = Integer.parseInt(calidad.getText().toString());
    }
}
