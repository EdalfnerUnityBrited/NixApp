package com.example.nixapp.UI.proveedor.ayuda;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.nixapp.R;

public class MenuAyudaProveedorFragment extends Fragment {
    Button pregunta1,pregunta2,pregunta3,pregunta4,pregunta5,pregunta6,pregunta7,pregunta8,pregunta9,pregunta10,pregunta11;
    AlertDialog.Builder informacion;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_de_ayuda_proveedor, container, false);
        pregunta1 = view.findViewById(R.id.pregunta1);
        pregunta2 = view.findViewById(R.id.pregunta2);
        pregunta3 = view.findViewById(R.id.pregunta3);
        pregunta4 = view.findViewById(R.id.pregunta4);
        pregunta5 = view.findViewById(R.id.pregunta5);
        pregunta6 = view.findViewById(R.id.pregunta6);
        pregunta7 = view.findViewById(R.id.pregunta7);
        pregunta8 = view.findViewById(R.id.pregunta8);
        pregunta9 = view.findViewById(R.id.pregunta9);
        pregunta10 = view.findViewById(R.id.pregunta10);
        pregunta11 = view.findViewById(R.id.pregunta11);

        pregunta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("Si los pagos se hacen en efectivo el cliente te los dara directamente, pero en caso de ser en tarjeta, estos nos llegaran a una cuenta a nosotros pero no te preocupes que todos los lunes se haran los depositos a los respectivos proveedores que hayan tenido alguna contratacion a lo largo de la semana a una cuenta.");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("Recomendamos que esto no sucesa, se llegara a pasar, se puede avisar al cliente por medio del chat que se creo, siempre siendo lo antes posible. En caso de solo hacerlo sin avisarle, este podra reportarlo y despues de dos reportes se suspendera la cuenta por tiempo idefinido (todo esto es Nix App 2.0)");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("En la pantalla principal (La que se te muestra al iniciar sesion), estara un circulo con una imagen la cual solo necesitas darle click y elegir una imagen de tu caleria como tu nueva imagen de perfil");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("Ese paso lo hace el anfitrion del evento al que entregaste tu servicios, solo se necesita avisarle que confirme tu llegada para que se proceda al pago de la liquidacion.");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("Habrá consecuencias para el proveedor que quedo mal");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("No se podra, hasta que se lleven a cabo las contrataciones solicitadas");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("Los correos de contacto directo son:\naz051128@gmail.com\nsoporteNixApp@gmail.com\na16300014@ceti.mx");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });

        return view;
    }
}
