package com.example.nixapp.UI.usuario.configUsuario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nixapp.DB.Pagos;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetodosPagoFragment extends Fragment {

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private CardInputWidget cardInputWidget;
    NixService nixService;
    NixClient nixClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view;
        view = inflater.inflate(R.layout.fragment_metodos_pago, container, false);
        Button payButton = view.findViewById(R.id.payButton);
         cardInputWidget= view.findViewById(R.id.cardInputWidget);
         retrofitInit();
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Me activaste", Toast.LENGTH_SHORT).show();
                pay();

            }
        });
        return view;
    }

    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    private void pay() {
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

        if (params == null) {
            return;
        }

        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        stripe = new Stripe(getContext(), PaymentConfiguration.getInstance(getContext()).getPublishableKey());
        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                String paymentMethodId = result.id;
                Pagos pagos= new Pagos(paymentMethodId);
                Call<ResponseBody> call = nixService.pagar(pagos);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getContext(), "Tarjeta añadida exitosamente", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Error al añadir la tarjeta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al añadir la tarjeta", Toast.LENGTH_SHORT).show();
                    }
                });
                // Send paymentMethodId to your server for the next steps
            }

            @Override
            public void onError(@NonNull Exception e) {
                // Display the error to the user
            }
        });


        }
}



