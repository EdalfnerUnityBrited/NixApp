package com.example.nixapp.conn;

import com.example.nixapp.DB.controllers.TokenController;
import com.example.nixapp.common.Constantes;
import com.example.nixapp.modelotablas.UsuarioToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NixClient {
    private static NixClient instance = null;
    private static NixService nixService;
    private Retrofit retrofit;

    public NixClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();
                Request.Builder builder = request.newBuilder()
                        .addHeader("Accept", "application/json");

                UsuarioToken token = TokenController.getToken();
                if (token != null)
                    builder.addHeader("Authorization", token.toString());

                return chain.proceed(builder.build());
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_NIXBASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        nixService = retrofit.create(NixService.class);
    }

    public static NixClient getInstance() {
        if (instance == null) {
            instance = new NixClient();
        }
        return instance;
    }

    public NixService getNixService() {
        return nixService;
    }

}
