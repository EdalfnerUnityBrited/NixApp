package com.example.nixapp.UI.usuario.ayuda;

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

public class MenuAyudaFragment extends Fragment {

    Button pregunta1,pregunta2,pregunta3,pregunta4,pregunta5,pregunta6,pregunta7,pregunta8,pregunta9,pregunta10,pregunta11,pregunta12;
    AlertDialog.Builder informacion;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_menu_de_ayuda, container, false);
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
        pregunta12 = view.findViewById(R.id.pregunta12);

        pregunta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informacion = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                informacion.setTitle("Respuesta:");
                informacion.setMessage("Justo en el apartados de 'Mis Eventos', hay un boton flotante con el signo de '+'.\nEste te abrira una ventana con la informacion basica que se necesita para crear tu evento. Despues de llenarla, solo deberas darle click en el boton y ¡Listo!.");
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
                informacion.setMessage("Cuando hayas creado tu evento, este aparecera en el apartado de 'Mis eventos', donde tendran dos botones de color morado, debes darle click al que tiene un dibujo de lapiz.\nEste te llevara a la edicion del evento, despues veras cuatro botones debajo del texto 'Compartir con:'(debes darle click al que tiene el mismo color que el fondo), al darle click se vera una linea para agregar Invitados, solo deberas ingresar el correo del usuario y ¡Listo!.");
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
                informacion.setMessage("Basicamente diferentes proveedores de muchos servicios agregaron sus productos para que estuvieran disponibles para ti y tus eventos. Asi que dejamos a tu alcance todos y cada uno de ellos para que desde la edicion de tu evento puedas cotizar y contratar cualquiera que necesites o quieras");
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
                informacion.setMessage("En el apartado de 'Mi Perfil', veras una foto dentro de un circulo.\nDebes darle click a ese circulo y enseguida se te pedira que elijas una imagen. Solo debes elegir tu nueva imagen de perfil de tu galeria y ¡Listo!.");
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
                informacion.setMessage("Al momento de estar creando tu evento, o al editarlo, hay un boton con la palabra 'Invitacion', solo debes darle click y te llevara inmediatamente a el creador de invitaciones.");
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
                informacion.setMessage("Al momento de crear un evento o de editarlo, aparece un texto que dice 'Correos', seguida de un espacio para escribir, en donde deberas escribir todos los correos (uno por uno y darle click en añadir) a los cuales les quieres mandar la invitacion de tu evento. \nDespues de agregarlos solo debes darle click al boton (en forma de sobre) debajo de los correos que añadiste y elegir por medio de que aplicacion deseas enviar el correo y ¡Listo!");
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
                informacion.setMessage("Debes ir a el apartado de 'Eventos proximos', darle click en el evento al que deseas confirmar tu asistencia, desplazar hacia abajo en donde encontraras el boton de 'Confirmar asistencia' y darle click.\nNOTA: Este boton solo aparecera el dia del evento despues de la hora indicada de inicio, y este valida que estes en la direccion que el anfitrion puso como cede del evento.");
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
                informacion.setMessage("Tu evento debera tener minimo 150 personas como cupo y debera tener el 60% de ese cupo de invitados asistentes en el mes que se publico el evento");
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
                informacion.setMessage("Se debera reportar a cualquiera de los correos de contacto directo de Soporte, se le devolvera el deposito que hizo (es parte de NixApp 2.0), habrá consecuencias para el proveedor que quedo mal y nos esforzaremos para que no pase denuevo");
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
                informacion.setMessage("El porcentaje que se cobra como 'Deposito' es el 15% del costo final del servicio");
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
                informacion.setMessage("No, esa informacion solo se usa para el cobro del servicio y no se almacena de ninguna manera");
                informacion.setCancelable(false);
                informacion.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                informacion.show();
            }
        });
        pregunta12.setOnClickListener(new View.OnClickListener() {
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
