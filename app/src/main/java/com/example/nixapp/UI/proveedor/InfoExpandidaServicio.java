package com.example.nixapp.UI.proveedor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.Calificacion;
import com.example.nixapp.DB.ContratacionExpandida;
import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico.EleccionPago;
import com.example.nixapp.UI.usuario.misEventos.EventosAdapter;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.CotizacionExpandidaResult;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoExpandidaServicio extends AppCompatActivity {

    String id_contratacion, estrellas;
    NixClient nixClient;
    NixService nixService;
    EditText nombre_evento,municipio,direccion,fecha,hora,nombre_servicio,estado_servicio,nombre_anf,correo_anf,telefono_anf;
    TextView desglose;
    Button pago_dep,pago_liquidacion, ir_pago,servicio_entregado,calificar_Servicio;
    ContratacionExpandida infoExpandida;
    Spinner spinners;
    private EventosAdapter mAdapter;
    private ArrayList<EventosItems> mEventsList;
    AlertDialog.Builder informacion;
    int ingreso = 0;
    TableRow botonesProveedor,botonesUsuario,botonCalificar;
    Calendar diaActual = Calendar.getInstance();
    AlertDialog.Builder dialogo1,dialog,dial;

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
            ingreso = (int) getIntent().getSerializableExtra("ingreso");
        }catch (Exception e)
        {
            id_contratacion = "";
            ingreso = 0;
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
        spinners = findViewById(R.id.spinnerSimple);
        botonesProveedor = findViewById(R.id.botonesProveedor);
        botonesUsuario = findViewById(R.id.botonesGeneral);
        botonCalificar = findViewById(R.id.botonesCalificacion);
        servicio_entregado = findViewById(R.id.LlegoProducto);
        ir_pago = findViewById(R.id.IrPagar);
        pago_dep = findViewById(R.id.PagoDeposito);
        pago_liquidacion = findViewById(R.id.PagoLiquidacion);
        calificar_Servicio = findViewById(R.id.CalificacionServicio);
        mAdapter = new EventosAdapter(this, mEventsList);
        spinners.setAdapter(mAdapter);
//////////////////////////////////// ingreso = 1 Proveedor entro

        if(ingreso == 1)
        {
            botonCalificar.setVisibility(View.GONE);
            botonesUsuario.setVisibility(View.GONE);
            pago_dep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    informacion = new AlertDialog.Builder(InfoExpandidaServicio.this);
                    informacion.setTitle("Importante:");
                    informacion.setMessage("Al dar click en 'Confirmar pago', se estara admitiendo que se recibio el pago en efectivo del deposito por parte del anfitrion del evento. ¿Esta SEGURO?");
                    informacion.setCancelable(false);
                    informacion.setPositiveButton("Confirmar Pago", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Contrataciones estado = new Contrataciones(id_contratacion,"pendiente");
                            Call<ResponseBody> cambiarEstado = nixService.cambioEstado(estado);
                            cambiarEstado.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful())
                                    {
                                        Toast.makeText(InfoExpandidaServicio.this,"Actualizacion exitosa",Toast.LENGTH_SHORT).show();
                                        infoExpandida.setEstado_servicio("pendiente");
                                        pago_liquidacion.setEnabled(false);
                                        pago_dep.setEnabled(false);
                                        estado_servicio.setText("pendiente");
                                    }
                                    else
                                    {
                                        Toast.makeText(InfoExpandidaServicio.this,"No se encontro la cotizacion",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(InfoExpandidaServicio.this,"Error al cambiar el estado del evento",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    informacion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    informacion.show();
                }
            });

            pago_liquidacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    informacion = new AlertDialog.Builder(InfoExpandidaServicio.this);
                    informacion.setTitle("Importante:");
                    informacion.setMessage("Al dar click en 'Confirmar pago', se estara admitiendo que se recibio el pago en efectivo de la liquidacion por parte del anfitrion del evento. ¿Esta SEGURO?");
                    informacion.setCancelable(false);
                    informacion.setPositiveButton("Confirmar Pago", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Contrataciones estado = new Contrataciones(id_contratacion,"pagado");
                            Call<ResponseBody> cambiarEstado = nixService.cambioEstado(estado);
                            cambiarEstado.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful())
                                    {
                                        Toast.makeText(InfoExpandidaServicio.this,"Actualizacion exitosa",Toast.LENGTH_SHORT).show();
                                        infoExpandida.setEstado_servicio("pagado");
                                        pago_liquidacion.setEnabled(false);
                                        pago_dep.setEnabled(false);
                                        estado_servicio.setText("pagado");

                                    }
                                    else
                                    {
                                        Toast.makeText(InfoExpandidaServicio.this,"No se encontro la cotizacion",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(InfoExpandidaServicio.this,"Error al cambiar el estado del evento",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    informacion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    informacion.show();
                }
            });
        }//////////////////////////////////ingreso 2 Usuario General netro
        else
        {
            botonesProveedor.setVisibility(View.GONE);
            servicio_entregado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] fechaSeparada = infoExpandida.getFecha().split("-");
                    int ano = Integer.valueOf(fechaSeparada[0]);
                    int mes = Integer.valueOf(fechaSeparada[1]);
                    int dia = Integer.valueOf(fechaSeparada[2]);
                    if(ano == diaActual.get(Calendar.YEAR)&& mes == (diaActual.get(Calendar.MONTH)+1)&&(diaActual.get(Calendar.DAY_OF_MONTH) == dia||diaActual.get(Calendar.DAY_OF_MONTH) == (dia-1)))
                    {
                        informacion = new AlertDialog.Builder(InfoExpandidaServicio.this);
                        informacion.setTitle("Importante:");
                        informacion.setMessage("Al dar click en 'Confirmar Llegada', se estara admitiendo que el Servicio '"+ infoExpandida.getNombre() +"' a entregado sus servicios. \n¿Esta SEGURO?");
                        informacion.setCancelable(false);
                        informacion.setPositiveButton("Confirmar Llegada", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Contrataciones estado = new Contrataciones(id_contratacion,"entregado");
                                Call<ResponseBody> cambiarEstado = nixService.cambioEstado(estado);
                                cambiarEstado.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toast.makeText(InfoExpandidaServicio.this,"Actualizacion exitosa",Toast.LENGTH_SHORT).show();
                                            infoExpandida.setEstado_servicio("entregado");
                                            estado_servicio.setText("entregado");
                                            ir_pago.setEnabled(true);
                                            servicio_entregado.setEnabled(false);

                                        }
                                        else
                                        {
                                            Toast.makeText(InfoExpandidaServicio.this,"No se encontro la cotizacion",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(InfoExpandidaServicio.this,"Error al cambiar el estado del evento",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        informacion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });
                        informacion.show();
                    }
                    else
                    {
                        Toast.makeText(InfoExpandidaServicio.this,"La fecha es muy lejana aun...",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ir_pago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(InfoExpandidaServicio.this, EleccionPago.class);
                    intent.putExtra("Pago", 2);
                    intent.putExtra("total del pago", infoExpandida.getDesglose());
                    intent.putExtra("id_cotizacion", id_contratacion);
                    startActivity(intent);
                }
            });
            calificar_Servicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(infoExpandida.getEstado_servicio().equals("solicitado"))
                    {
                        Toast.makeText(InfoExpandidaServicio.this,"No puedes calificar un evento del que aun no pagas el deposito",Toast.LENGTH_SHORT).show();
                    }
                    else if(infoExpandida.getEstado_servicio().equals("pendiente"))
                    {
                        Toast.makeText(InfoExpandidaServicio.this,"No puedes calificar un evento que aun no a sido entregado",Toast.LENGTH_SHORT).show();
                    }
                    else if(infoExpandida.getEstado_servicio().equals("entregado"))
                    {
                        Toast.makeText(InfoExpandidaServicio.this,"Debes liquidarlo para poder calificarlo <3",Toast.LENGTH_SHORT).show();
                    }
                    else if(infoExpandida.getEstado_servicio().equals("pagado"))
                    {
                        dialogo1 = new AlertDialog.Builder(InfoExpandidaServicio.this);
                        dialogo1.setTitle("Encuesta del Servicio:");
                        dialogo1.setMessage("¿ Llego el servicio ?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                final String[] listItems = {"1", "2", "3", "4", "5"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(InfoExpandidaServicio.this);
                                builder.setTitle("Del 1 al 5, ¿Que calificacion le pondrias?");
                                int checkedItem = 0; //this will checked the item when user open the dialog
                                builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(InfoExpandidaServicio.this, "Position: " + which + " Value: " + listItems[which], Toast.LENGTH_LONG).show();
                                        estrellas= listItems[which];
                                    }
                                });
                                builder.setPositiveButton("Siguiente", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Calificacion calificacion= new Calificacion(id_contratacion, estrellas);
                                        Call<ResponseBody> call= nixService.calificar(calificacion);
                                        call.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()){
                                                    Toast.makeText(InfoExpandidaServicio.this, "Calificado", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(InfoExpandidaServicio.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                                        dial = new AlertDialog.Builder(InfoExpandidaServicio.this);
                                        dial.setTitle("Comentario");
                                        dial.setMessage("Ingrese algun comentario acerca el servicio que recibio:");
                                        LinearLayout layout = new LinearLayout(InfoExpandidaServicio.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        final EditText titleBox = new EditText(InfoExpandidaServicio.this);
                                        titleBox.setHint("Comentario...");
                                        titleBox.setTypeface(Typeface.DEFAULT);
                                        layout.addView(titleBox);
                                        dial.setView(layout);
                                        dial.setCancelable(false);
                                        dial.setPositiveButton("Finalizar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        dial.show();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                        dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(InfoExpandidaServicio.this,"¿Encerio?... Pero si ya está pagado",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogo1.show();
                    }
                }
            });
        }
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
                        ir_pago.setEnabled(false);
                        servicio_entregado.setEnabled(false);
                    }
                    else if(infoExpandida.getEstado_servicio().equals("pendiente"))
                    {
                        ir_pago.setEnabled(false);
                        pago_liquidacion.setEnabled(false);
                        pago_dep.setEnabled(false);
                    }
                    else if(infoExpandida.getEstado_servicio().equals("entregado"))
                    {
                        pago_dep.setEnabled(false);
                        servicio_entregado.setEnabled(false);
                    }
                    else if(infoExpandida.getEstado_servicio().equals("pagado"))
                    {
                        ir_pago.setEnabled(false);
                        servicio_entregado.setEnabled(false);
                        pago_liquidacion.setEnabled(false);
                        pago_dep.setEnabled(false);
                    }
                }
                else
                {
                    Toast.makeText(InfoExpandidaServicio.this,"Error al obtener la contratacion",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CotizacionExpandidaResult> call, Throwable t) {
                Toast.makeText(InfoExpandidaServicio.this,"Error al la informacion de esa contratacion",Toast.LENGTH_SHORT).show();
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
