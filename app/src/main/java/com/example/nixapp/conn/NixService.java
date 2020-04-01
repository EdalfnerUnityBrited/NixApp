package com.example.nixapp.conn;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.conn.results.ChatResult;
import com.example.nixapp.conn.results.EventosResult;
import com.example.nixapp.conn.results.LoginResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NixService {

    @POST("auth/signup")
    Call<ResponseBody> usuario(@Body Usuario usuarios);

    @POST("auth/login")
    Call<LoginResult> login(@Body Usuario usuarios);

    @GET("chats")
    Call<ChatResult> chat();

    @POST("eventos")
    Call<EventosResult> eventos(@Body Eventos eventos);

    @GET("auth/user")
    Call<Usuario> getUser();

}
