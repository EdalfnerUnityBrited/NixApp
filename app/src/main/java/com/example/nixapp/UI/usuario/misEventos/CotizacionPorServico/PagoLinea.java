package com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.DB.Chat;
import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.DB.Pagos;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagoLinea extends AppCompatActivity {

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private CardMultilineWidget cardMultilineWidget;
    NixService nixService;
    NixClient nixClient;
    TextView proveedor, cantidad;
    double pago_total;
    String tipo_bono;
    String id_contratacion;
    AlertDialog.Builder informacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_linea);
        Button payButton = findViewById(R.id.payButton);
        cardMultilineWidget= findViewById(R.id.cardMultilineWidget);
        cardMultilineWidget.setCardHint("Numero de la tarjeta");
        proveedor= findViewById(R.id.servicio);
        cantidad= findViewById(R.id.precio);
        proveedor.setText("Para el servicio: \n'"+CotizacionServicio.nombreServicio+"'");
        cantidad.setText("Cargo total: $"+ CotizacionServicio.costoTotal+" MXN");
        pago_total=(double) getIntent().getSerializableExtra("Cargo");
        tipo_bono=(String) getIntent().getSerializableExtra("tipo_bono");
        id_contratacion=(String) getIntent().getSerializableExtra("id_cotizacion");
        retrofitInit();
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipo_bono.equals("el Deposito"))
                {
                    informacion = new AlertDialog.Builder(PagoLinea.this);
                    informacion.setTitle("Confirmacion de Pago:");
                    informacion.setMessage("Se procedera a hacer el cobro en la tarjeta que se agrego... ¿Desea continuar?");
                    informacion.setCancelable(false);
                    informacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            payDeposito();
                        }
                    });
                    informacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    informacion.show();

                }
                else
                {
                    informacion = new AlertDialog.Builder(PagoLinea.this);
                    informacion.setTitle("Confirmacion de Pago:");
                    informacion.setMessage("Se procedera a hacer el cobro en la tarjeta que se agrego... ¿Desea continuar?");
                    informacion.setCancelable(false);
                    informacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            payLiquidacion();
                        }
                    });
                    informacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    informacion.show();

                }

            }
        });
    }

    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    private void payLiquidacion() {
        PaymentMethodCreateParams params = cardMultilineWidget.getPaymentMethodCreateParams();

        if (params == null) {
            return;
        }

        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        stripe = new Stripe(PagoLinea.this, PaymentConfiguration.getInstance(PagoLinea.this).getPublishableKey());
        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                double montoPago= pago_total;
                montoPago= montoPago*100;
                String paymentMethodId = result.id;
                Pagos pagos= new Pagos(paymentMethodId, "Liquidacion del pago del servicio con ID: "+id_contratacion,montoPago);
                Call<ResponseBody> call = nixService.pagar(pagos);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {

                            Contrataciones estado = new Contrataciones(id_contratacion,"pagado");
                            Call<ResponseBody> cambiarEstado = nixService.cambioEstado(estado);
                            cambiarEstado.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful())
                                    {
                                        Toast.makeText(PagoLinea.this,"Liquidacion exitosa, recarga la ventana",Toast.LENGTH_LONG).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(PagoLinea.this,"No se encontro la cotizacion",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(PagoLinea.this,"Error al cambiar el estado del evento",Toast.LENGTH_SHORT).show();
                                }
                            });
                            PagoLinea.this.finish();
                        }
                        else
                        {
                            Toast.makeText(PagoLinea.this, "Transferencia Fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(PagoLinea.this, "Error al hacer el pago", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(@NonNull Exception e) {
                // Display the error to the user
                Toast.makeText(PagoLinea.this, "Error al aceptar tarjeta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void payDeposito() {
        PaymentMethodCreateParams params = cardMultilineWidget.getPaymentMethodCreateParams();

        if (params == null) {
            return;
        }

        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        stripe = new Stripe(PagoLinea.this, PaymentConfiguration.getInstance(PagoLinea.this).getPublishableKey());
        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                double montoPago= pago_total;
                montoPago= montoPago*100;
                String paymentMethodId = result.id;
                Pagos pagos= new Pagos(paymentMethodId, "Adelanto del pago de: "+CotizacionServicio.nombreServicio+" del proveedor "+CotizacionServicio.nombreProvee,montoPago);
                Call<ResponseBody> call = nixService.pagar(pagos);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(PagoLinea.this, "Pago Realizado", Toast.LENGTH_SHORT).show();
                            Chat chat= new Chat(CotizacionServicio.idService);
                            Call<ResponseBody> callChat = nixService.nuevoChat(chat);
                            callChat.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()){

                                    }
                                    else{
                                        Toast.makeText(PagoLinea.this, "Error en chats", Toast.LENGTH_SHORT).show();
                                        Log.i("Error chat",response.errorBody().toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                            Contrataciones contratacion= new Contrataciones("linea",CotizacionServicio.idService, CotizacionServicio.desglose,String.valueOf(CotizacionServicio.idEvento));
                            Call<ResponseBody> callContract = nixService.nuevaContratacion(contratacion);
                            callContract.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(PagoLinea.this, "Contratación exitosa, Detalles disponibles en tus Servicios Proximos", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(PagoLinea.this, "Error en la contratación", Toast.LENGTH_SHORT).show();
                                        Log.i("Error contr",response.errorBody().toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }
                        else{
                            Toast.makeText(PagoLinea.this, "Error al validar la tarjeta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(PagoLinea.this, "Error al validar la tarjeta", Toast.LENGTH_SHORT).show();
                    }
                });
                // Send paymentMethodId to your server for the next steps
            }
            @Override
            public void onError(@NonNull Exception e) {
                // Display the error to the user
                Toast.makeText(PagoLinea.this, "Error al aceptar tarjeta", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
