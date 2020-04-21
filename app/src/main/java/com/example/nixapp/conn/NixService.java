package com.example.nixapp.conn;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.DB.Pagos;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.conn.results.ChatResult;
import com.example.nixapp.conn.results.EventosListResult;
import com.example.nixapp.conn.results.EventosResult;
import com.example.nixapp.conn.results.EventosTodosResult;
import com.example.nixapp.conn.results.ImagenResult;
import com.example.nixapp.conn.results.LoginResult;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface NixService {

    @POST("auth/signup")
    Call<ResponseBody> usuario(@Body Usuario usuarios);

    @POST("auth/login")
    Call<LoginResult> login(@Body Usuario usuarios);

    @GET("chats")
    Call<ChatResult> chat();

    @POST("eventos")
    Call<EventosResult> eventos(@Body Eventos eventos);

    @GET("eventos/todos")
    Call<EventosTodosResult> eventosTodos();

    @GET("eventos/usuario")
    Call<EventosListResult> eventosUsuario();

    @GET("auth/user")
    Call<Usuario> getUser();

    @PUT("auth/foto")
    Call<ResponseBody> foto(@Body Usuario usuario);

    @PUT("auth/user")
    Call<ResponseBody> datos(@Body Usuario usuario);

    @PUT("auth/password")
    Call<ResponseBody> pass(@Body Usuario usuario);

    @POST("imagen")
    Call<ResponseBody> image(@Body List<ImagenEventos> imagenEventos);

    @POST ("eventos/buscar")
    Call<EventosResult> eventoBuscar(@Body Eventos ingresar);

    @HTTP(method = "DELETE", path = "eventos", hasBody = true)
    Call<ResponseBody> deleteEvent(@Body Eventos eventos);

    @GET("eventos/asistencia")
    Call<EventosListResult> eventosAsistenciaUsuario();

    @POST("eventos/evento")
    Call<ImagenResult> buscarImagenes(@Body Eventos evento_imagenes);

    @POST("auth/pagar")
    Call<ResponseBody> pagar(@Body Pagos pagos);
}
