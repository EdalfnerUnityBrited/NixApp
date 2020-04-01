package com.example.nixapp.UI.usuario.misEventos;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearEvento extends AppCompatActivity {

    NixService nixService;
    NixClient nixClient;
    String nombre_evento;
    int privacidad;
    int categoria_evento;
    String fecha;
    String hora;
    String lugar;
    String descripcion;
    int cupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
        retrofitinit();
        nombre_evento="peda";
        privacidad=0;
        categoria_evento=3;
        fecha="2020-04-12";
        hora="12:00";
        lugar="la santa zapopan jalisco mexico";
        descripcion="hasta que el cuerpo aguante";
        cupo=300;
        final Eventos requestSample = new Eventos(nombre_evento, privacidad, categoria_evento, fecha, hora, lugar, descripcion, cupo);
        Call<EventosResult> call = nixService.eventos(requestSample);
        call.enqueue(new Callback<EventosResult>() {
            @Override
            public void onResponse(Call<EventosResult> call, Response<EventosResult> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CrearEvento.this, "Crear evento correcto", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(CrearEvento.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                    try {
                        Log.i("Error",response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EventosResult> call, Throwable t) {
                Toast.makeText(CrearEvento.this, "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

}
