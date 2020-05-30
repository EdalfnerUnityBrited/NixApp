package com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Citas;
import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.serviciosContratados.ServiciosProximos;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;

import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EleccionPago extends AppCompatActivity {

    Button efectivo, linea, hacer_cita;
    int tipo_bono;
    String total_pago;
    double cargo_total;
    AlertDialog.Builder resumen_compra;
    String tipo = "",id_cotizacion;
    TextView fecha,fecha_text,hora,hora_text,titulo;
    TimePickerDialog picker2;
    DatePickerDialog picker;
    int  dia, ano, mes;
    Calendar diaActual = Calendar.getInstance();
    NixService nixService;
    NixClient nixClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_pago);
        efectivo= findViewById(R.id.botonEfectivo);
        linea= findViewById(R.id.botonLinea);
        retrofitInit();
        ////////////////////////////////////////////////
        hacer_cita = findViewById(R.id.botonhacercita);
        fecha_text = findViewById(R.id.text_fecha);
        hora_text = findViewById(R.id.text_hora);
        titulo = findViewById(R.id.textView4);
        ////////////////////////////////////////////////
        fecha = findViewById(R.id.fecha_cita);
        fecha.setInputType(InputType.TYPE_NULL);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                final Calendar cldr = Calendar.getInstance();
                dia = cldr.get(Calendar.DAY_OF_MONTH);
                mes = cldr.get(Calendar.MONTH);
                ano = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EleccionPago.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fecha.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, ano, mes, dia);
                picker.show();
            }
        });
        hora = findViewById(R.id.hora_cita);
        hora.setInputType(InputType.TYPE_NULL);
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale2 = getResources().getConfiguration().locale;
                Locale.setDefault(locale2);
                final Calendar cldr2 = Calendar.getInstance();
                int hour = cldr2.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr2.get(Calendar.MINUTE);
                // time picker dialog
                picker2 = new TimePickerDialog(EleccionPago.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String minutos;
                                String horas;
                                if(sMinute < 10) {
                                    minutos = "0" + String.valueOf(sMinute);
                                    hora.setText(sHour + ":" + minutos);

                                }
                                else if(sHour < 10)
                                {
                                    horas = "0" + String.valueOf(sHour);
                                    hora.setText(horas + ":" + sMinute);
                                }
                                else if(sHour <0 && sMinute< 0)
                                {
                                    minutos = "0" + String.valueOf(sMinute);
                                    horas = "0" + String.valueOf(sHour);
                                    hora.setText(horas + ":" + minutos);
                                }
                                else
                                {
                                    hora.setText(sHour + ":" + sMinute);
                                }
                            }
                        }, hour, minutes, true);
                picker2.show();
            }
        });

        ///////////////////////////////////////////////
        hacer_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true)
                {
                    String fechaCita= fecha.getText().toString();


                    String horaCita= hora.getText().toString()+":00";
                    int idSer= Integer.parseInt(CotizacionServicio.idService);
                    /*Verificar que no sea antes del dia actual, ni despues de una semana antes del evento, Tienes el
                    * de la cotizacion creo que se debera hacer una llamada para verificar eso, despues se debe verificar
                    * que la fecha no la tenga ocupada... y ya se crea la cita...*/
                    Citas citas= new Citas(horaCita, fechaCita,  idSer, CotizacionServicio.idEvento);
                    Call<ResponseBody> call= nixService.nuevaCita(citas);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Contrataciones contratacion= new Contrataciones("efectivo",CotizacionServicio.idService, CotizacionServicio.desglose,String.valueOf(CotizacionServicio.idEvento));
                                Call<ResponseBody> callContr = nixService.nuevaContratacion(contratacion);
                                callContr.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(EleccionPago.this, "Contratacion exitosa", Toast.LENGTH_SHORT).show();
                                            Intent intent= new Intent(EleccionPago.this, ServiciosProximos.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(EleccionPago.this, "Error en los datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            else{
                                Toast.makeText(EleccionPago.this, "Elija otra fecha", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }
                else
                {

                }
            }
        });
        //////////////////////////////////////////////

        tipo_bono=(int) getIntent().getSerializableExtra("Pago");
        total_pago=(String) getIntent().getSerializableExtra("total del pago");
        id_cotizacion = (String) getIntent().getSerializableExtra("id_cotizacion");
        if(tipo_bono == 1)
        {
            cargo_total = (Float.valueOf(total_pago) * 0.1);
            tipo = "el Deposito";
        }
        else
        {
            String[] separado1 = total_pago.split("\\$");
            String[] separado2 = separado1[1].split("MXN");
            cargo_total = (Float.valueOf(separado2[0])*0.9);
            tipo = "la Liquidacion";

        }

        linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumen_compra = new AlertDialog.Builder(EleccionPago.this);
                resumen_compra.setTitle("Importe total:");
                resumen_compra.setMessage("Se cobrara el importe de: $" + cargo_total + "  MXN \nSimbolizando " + tipo + " del Servicio");
                resumen_compra.setCancelable(false);
                resumen_compra.setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Intent intent= new Intent(EleccionPago.this, PagoLinea.class);
                        intent.putExtra("Cargo", cargo_total);
                        intent.putExtra("tipo_bono", tipo);
                        intent.putExtra("id_cotizacion", id_cotizacion);
                        startActivity(intent);
                        EleccionPago.this.finish();
                    }
                });
                resumen_compra.setNegativeButton("No, es incorrecto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                resumen_compra.show();

            }
        });
        efectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumen_compra = new AlertDialog.Builder(EleccionPago.this);
                resumen_compra.setTitle("Importe total:");
                resumen_compra.setMessage("Se debera pagar el importe de: $" + cargo_total + "  MXN Simbolizando " + tipo + " del Servicio.\nSe pasara a elegir una fecha en la cual se hara la cita para dar el deposito en efectivo.\n¿Esta deacuerdo?");
                resumen_compra.setCancelable(false);
                resumen_compra.setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        efectivo.setEnabled(false);
                        linea.setEnabled(false);
                        /////////////////////////////////////////////////
                        hacer_cita.setVisibility(View.VISIBLE);
                        fecha_text.setVisibility(View.VISIBLE);
                        hora_text.setVisibility(View.VISIBLE);
                        titulo.setVisibility(View.VISIBLE);
                        ////////////////////////////////////////////////
                        fecha.setVisibility(View.VISIBLE);
                        hora.setVisibility(View.VISIBLE);
                    }
                });
                resumen_compra.setNegativeButton("No, es incorrecto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                resumen_compra.show();
            }
        });
    }

    private boolean verificarFecha(int day, int month, int year) {
        int diff=0;
        if (diaActual.get(Calendar.YEAR)>year){
            fecha.setError("Esa fecha se encuentra en el pasado");
            return true;
        }
        else{
            if ((diaActual.get(Calendar.MONTH) +1)> month ||
                    ((diaActual.get(Calendar.MONTH) +1) == month && diaActual.get(Calendar.DAY_OF_MONTH) > day)) {
                fecha.setError("Esa fecha se encuentra en el pasado");
                return true;
            }
            else{
                if((diaActual.get(Calendar.MONTH) +1) == month)
                {
                    diff=day-diaActual.get(Calendar.DAY_OF_MONTH);
                    if (diff<3){
                        fecha.setError("Tienes que tener 3 dias de anticipación");
                        return true;
                    }
                }

            }
            return false;
        }
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
}
