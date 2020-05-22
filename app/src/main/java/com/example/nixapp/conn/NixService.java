package com.example.nixapp.conn;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.Busqueda;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.ImagenArticulo;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.DB.ImagenPaquete;
import com.example.nixapp.DB.Pagos;
import com.example.nixapp.DB.PaqueteArticulo;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.DB.Prospectos;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.DB.ZonaServicio;
import com.example.nixapp.conn.results.ArticuloResult;
import com.example.nixapp.conn.results.ArticulosListResult;
import com.example.nixapp.conn.results.ChatResult;
import com.example.nixapp.conn.results.EventoLlenoResult;
import com.example.nixapp.conn.results.EventosListResult;
import com.example.nixapp.conn.results.EventosResult;
import com.example.nixapp.conn.results.EventosTodosResult;
import com.example.nixapp.conn.results.ImagenResult;
import com.example.nixapp.conn.results.LoginResult;
import com.example.nixapp.conn.results.NotificationsResult;
import com.example.nixapp.conn.results.PaqueteResult;
import com.example.nixapp.conn.results.ProspectosResult;
import com.example.nixapp.conn.results.ServicioResult;
import com.example.nixapp.conn.results.UsuarioListResult;
import com.example.nixapp.conn.results.UsuarioResult;

import java.util.List;

import okhttp3.Response;
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

    @GET("chats/usuario")
    Call<ChatResult> chat();

    @GET("chats/proveedor")
    Call<ChatResult> chatProveedor();

    @POST("eventos")
    Call<EventosResult> eventos(@Body Eventos eventos);

    @GET("eventos/todos")
    Call<EventosTodosResult> eventosTodos();

    @GET("eventos/usuario")
    Call<EventosListResult> eventosUsuario();

    @GET("eventos/tendencia")
    Call<EventosListResult> eventosTendencia();

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

    @POST("eventos/asistir")
    Call<ResponseBody> prospecto(@Body Prospectos prospectos);

    @POST("eventos/ir")
    Call<ProspectosResult> Confirmacionprospecto(@Body Prospectos prospectos);

    @POST("eventos/buscarEvento")
    Call<EventosListResult> buscarEvento(@Body Busqueda busqueda);

    @POST("eventos/cupo")
    Call<EventoLlenoResult> eventoLleno(@Body Eventos evento);

    @POST("auth/verificar")
    Call<UsuarioResult> verificacionEmail(@Body Usuario usuario);

    @POST("auth/signupfg")
    Call<ResponseBody> CrearUsuarioFG(@Body Usuario usuario);

    @POST("eventos/idEvento")
    Call<EventosResult> buscarEventoId(@Body Busqueda busqueda);

    @POST("eventos/actualizar")
    Call<ResponseBody> actualizarEvento(@Body Eventos eventos);

    @POST("eventos/invitar")
    Call<ResponseBody> invitar(@Body Busqueda busqueda);

    @POST("imagen/unaImagen")
    Call<ResponseBody> imagenUna(@Body ImagenEventos imagenEventos);

    @POST("imagen/erase")
    Call<ResponseBody> borrarImagen(@Body ImagenEventos imagenEventos);

    @GET("notificaciones")
    Call<NotificationsResult> notificaciones();

    @POST("eventos/id")
    Call<EventosResult> getEventId(@Body Busqueda busqueda);

    @POST("eventos/creador")
    Call<UsuarioResult> infoAnfitrion (@Body Eventos eventos);

    @POST("eventos/invitados")
    Call<UsuarioListResult> confirmados (@Body Eventos eventos);

    @POST("proveedor/articulo")
    Call<ArticuloResult> nuevoArticulo(@Body Articulos articulos);

    @POST("proveedor/nuevoServicio")
    Call<ServicioResult> crearServicio(@Body CatalogoServicios catalogoServicios);

    @POST("proveedor/zonaServicio")
    Call<ResponseBody> munServicio(@Body List<ZonaServicio> zonaServicio);

    @POST("imagen/articulo")
    Call<ResponseBody> imagenArticulo(@Body List<ImagenArticulo> imagenArticulos);

    @POST("proveedor/paquete")
    Call<PaqueteResult> nuevoPaquete(@Body Paquetes paquetes);

    @POST("imagen/paquete")
    Call<ResponseBody> imagenPaquete(@Body List<ImagenPaquete> imagenPaquetes);

    @POST("proveedor/paqueteArticulo")
    Call<ResponseBody> paqueteArticulo(@Body List<PaqueteArticulo> paqueteArticulos);

    @POST("proveedor/articuloServicio")
    Call<ArticulosListResult> articulosServicio(@Body Articulos articulos);
}
