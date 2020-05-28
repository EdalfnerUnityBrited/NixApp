package com.example.nixapp.UI.proveedor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.ContratacionExpandida;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.EventosAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.CotizacionExpandidaResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoExpandidaServicio extends AppCompatActivity {

    String id_contratacion;
    NixClient nixClient;
    NixService nixService;
    EditText nombre_evento,municipio,direccion,fecha,hora,nombre_servicio,estado_servicio,nombre_anf,correo_anf,telefono_anf;
    TextView desglose;
    Button pago_dep,pago_liquidacion;
    ContratacionExpandida infoExpandida;
    Spinner spinners;
    private EventosAdapter mAdapter;
    private ArrayList<EventosItems> mEventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_expandida_servicio);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Contratacion Desglozada");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        retrofitinit();
        try {
            id_contratacion = (String) getIntent().getSerializableExtra("id_contratacion");
        }catch (Exception e)
        {
            id_contratacion = "";
        }
        initList();
        nombre_evento = findViewById(R.id.nomb);
        municipio = findViewById(R.id.muni);
        direccion = findViewById(R.id.direcc);
        fecha = findViewById(R.id.fecha);
        hora = findViewById(R.id.hora_evento);
        nombre_servicio = findViewById(R.id.nombre_servicioText);
        estado_servicio = findViewById(R.id.estado_servicio);
        desglose = findViewById(R.id.desglozado);
        nombre_anf = findViewById(R.id.nomb_Anf);
        correo_anf = findViewById(R.id.email_Anf);
        telefono_anf = findViewById(R.id.tels_Anf);
        pago_dep = findViewById(R.id.PagoDeposito);
        pago_liquidacion = findViewById(R.id.PagoLiquidacion);
        spinners = findViewById(R.id.spinnerSimple);
        mAdapter = new EventosAdapter(this, mEventsList);
        spinners.setAdapter(mAdapter);


        Articulos contratacionInfo = new Articulos(Integer.valueOf(id_contratacion));
        final Call<CotizacionExpandidaResult> informacionExpandida = nixService.contratatacionExpandida(contratacionInfo);
        informacionExpandida.enqueue(new Callback<CotizacionExpandidaResult>() {
            @Override
            public void onResponse(Call<CotizacionExpandidaResult> call, Response<CotizacionExpandidaResult> response) {
                if(response.isSuccessful())
                {
                    infoExpandida = response.body().contrataciones;
                    spinners.setSelection(infoExpandida.getCategoria_evento());
                    spinners.setEnabled(false);
                    nombre_evento.setText(infoExpandida.getNombre_evento());
                    nombre_evento.setBackground(null);
                    nombre_evento.setEnabled(false);
                    municipio.setText(infoExpandida.getMunicipio());
                    municipio.setBackground(null);
                    municipio.setEnabled(false);
                    direccion.setText(infoExpandida.getLugar());
                    direccion.setBackground(null);
                    direccion.setEnabled(false);
                    fecha.setText(infoExpandida.getFecha());
                    fecha.setBackground(null);
                    fecha.setEnabled(false);
                    hora.setText(infoExpandida.getHora());
                    hora.setBackground(null);
                    hora.setEnabled(false);
                    nombre_servicio.setText(infoExpandida.getNombre());
                    nombre_servicio.setBackground(null);
                    nombre_servicio.setEnabled(false);
                    estado_servicio.setText(infoExpandida.getEstado_servicio());
                    estado_servicio.setBackground(null);
                    estado_servicio.setEnabled(false);
                    desglose.setText(infoExpandida.getDesglose());
                    desglose.setBackground(null);
                    desglose.setEnabled(false);
                    nombre_anf.setText(infoExpandida.getName() + " " + infoExpandida.getApellidoP() + " " + infoExpandida.getApellidoM());
                    nombre_anf.setBackground(null);
                    nombre_anf.setEnabled(false);
                    correo_anf.setText(infoExpandida.getEmail());
                    correo_anf.setBackground(null);
                    correo_anf.setEnabled(false);
                    telefono_anf.setText(infoExpandida.getTelefono());
                    telefono_anf.setBackground(null);
                    telefono_anf.setEnabled(false);
                    if(infoExpandida.getEstado_servicio().equals("solicitado"))
                    {
                        pago_liquidacion.setEnabled(false);
                    }
                    else if(infoExpandida.getEstado_servicio().equals("pendiente"))
                    {
                        pago_liquidacion.setEnabled(false);
                        pago_dep.setEnabled(false);
                    }
                    else if(infoExpandida.getEstado_servicio().equals("Entregado"))
                    {
                        pago_dep.setEnabled(false);
                    }
                    else if(infoExpandida.getEstado_servicio().equals("Pagado"))
                    {
                        pago_liquidacion.setEnabled(false);
                        pago_dep.setEnabled(false);
                    }
                }
                else
                {
                    Toast.makeText(InfoExpandidaServicio.this,"Error al obtener la contratacion",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CotizacionExpandidaResult> call, Throwable t) {
                Toast.makeText(InfoExpandidaServicio.this,"Error al la informacion de esa contratacion",Toast.LENGTH_LONG).show();
            }
        });


    }
    private void initList() {
        mEventsList = new ArrayList<>();
        mEventsList.add(new EventosItems("Elige una categoria", R.drawable.select_some));
        mEventsList.add(new EventosItems("Compromisos", R.drawable.compromisos));
        mEventsList.add(new EventosItems("Mega Eventos", R.drawable.mega));
        mEventsList.add(new EventosItems("Galas", R.drawable.galas));
        mEventsList.add(new EventosItems("Empresariales", R.drawable.empresariales));
        mEventsList.add(new EventosItems("Festejos", R.drawable.festejos));
        mEventsList.add(new EventosItems("Religiosos", R.drawable.religiosos));
    }

    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
