package com.example.nixapp.UI.usuario.configUsuario;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nixapp.R;

import java.util.Calendar;
import java.util.Date;

public class InformacionPersonalFragment extends Fragment {

    private static final String TAG = "MainActivity";
    private TextView mDisplayDate=null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int ano, mes, dia;
    Date currentTime = Calendar.getInstance().getTime();
    String date;
    boolean cambio = true,cambio2 = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        View view;
        view = inflater.inflate(R.layout.fragment_informacion_personal, container, false);
        mDisplayDate = (TextView) view.findViewById(R.id.fecha_nacimiento);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();



            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "-" + day + "-" + year);
                ano=year;
                mes=month;
                dia=day;
                date = year + "-" + month + "-" + day;
                mDisplayDate.setText(date);
            }
        };

        final EditText pass = view.findViewById(R.id.password_usuario);
        final EditText pass_conf = view.findViewById(R.id.confirmacion_contra);
        final View row = view.findViewById(R.id.confirmacion);
        final View view2 = view.findViewById(R.id.view2);
        view2.setVisibility(View.GONE);

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true)
                {
                    pass.setHint("Nueva Contraseña");
                }
                else
                {
                    pass.setHint("¿Desea cambiar su contraseña?");
                }
            }

        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String text = pass.getText().toString();
                if(text.equals(""))
                {
                    animation(row);
                    pass_conf.setText("");
                    view2.setVisibility(View.GONE);
                    cambio = true;

                }
                else{
                    if(cambio == true)
                    {
                        animation1(row);
                        view2.setVisibility(View.VISIBLE);
                        cambio = false;
                    }

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        //////////////////////////////////////////////////////////////////////// //
        final EditText nombre = view.findViewById(R.id.nombre_user);
        final EditText apP = view.findViewById(R.id.ap_pat_user);
        final EditText apM = view.findViewById(R.id.ap_mat_user);
        final View rowapP = view.findViewById(R.id.apellido_paterno);
        final View rowapM = view.findViewById(R.id.apellido_materno);
        final View invi = view.findViewById(R.id.inv1);
        final View invi2 = view.findViewById(R.id.invi2);

        nombre.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true)
                {
                    nombre.setHint("Nuevo nombre");
                }
                else
                {
                    nombre.setHint("¿Quieres Editar tu nombre?");
                }
            }

        });

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String text2 = nombre.getText().toString();
                if(text2.equals(""))
                {

                    animation(rowapP);
                    animation(rowapM);
                    apM.setText("");
                    apP.setText("");
                    invi.setVisibility(View.GONE);
                    invi2.setVisibility(View.GONE);
                    cambio2 = true;

                }
                else{
                    if(cambio2 == true)
                    {
                        animation1(rowapP);
                        animation1(rowapM);
                        invi.setVisibility(View.VISIBLE);
                        invi2.setVisibility(View.VISIBLE);
                        cambio2 = false;
                    }

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        return view;
    }

    public void animation(View view)
    {
        view.setVisibility(View.GONE);
    }
    public void animation1(View row)
    {
        Animation mLoadAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);
        row.startAnimation(mLoadAnimation);
        row.setVisibility(View.VISIBLE);
    }
}
