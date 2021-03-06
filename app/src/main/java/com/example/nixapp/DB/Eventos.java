package com.example.nixapp.DB;

public class Eventos {
    String id;
    String nombre_evento;
    int privacidad;
    int categoria_evento;
    String fecha;
    String hora;
    String lugar;
    String descripcion;
    int cupo;
    int cover;

    public Eventos(String id, String nombre_evento, int privacidad, int categoria_evento, String fecha, String hora, String lugar, String descripcion, int cupo, int cover, String fotoPrincipal, String municipio) {
        this.id = id;
        this.nombre_evento = nombre_evento;
        this.privacidad = privacidad;
        this.categoria_evento = categoria_evento;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.cover = cover;
        this.fotoPrincipal = fotoPrincipal;
        this.municipio = municipio;
    }

    public Eventos(int privacidad, String nombre_evento, int categoria_evento, String fecha, String hora, String lugar, String descripcion, int cupo, int cover, String fotoPrincipal, String municipio)
    {
        this.nombre_evento = nombre_evento;
        this.privacidad = privacidad;
        this.categoria_evento = categoria_evento;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.cover = cover;
        this.fotoPrincipal = fotoPrincipal;
        this.municipio = municipio;
    }

    int tendencia;
    String fotoPrincipal;
    String estado;
    String municipio;

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }



    public Eventos(String nombre_evento, int privacidad, int categoria_evento, String fecha, String hora, String lugar, String descripcion, int cupo, int cover, String foto) {
        this.nombre_evento = nombre_evento;
        this.privacidad = privacidad;
        this.categoria_evento = categoria_evento;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.cupo=cupo;
        this.cover=cover;
        this.fotoPrincipal =foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public void setFotoPrincipal(String fotoPrincipal) {
        this.fotoPrincipal = fotoPrincipal;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Eventos(String nombre_evento)
    {
        this.nombre_evento = nombre_evento;
    }

    public Eventos(String nombre_evento, int privacidad, int categoria_evento, String fecha, String hora, String lugar, String descripcion, int cupo, int cover, String foto,String estado)
    {
        this.nombre_evento = nombre_evento;
        this.privacidad = privacidad;
        this.categoria_evento = categoria_evento;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.cupo=cupo;
        this.cover=cover;
        this.fotoPrincipal =foto;
        this.estado = estado;
        this.tendencia = 0;

    }

    public Eventos(String id, String nombre_evento, int privacidad, int categoria_evento, String fecha, String hora, String lugar, String descripcion, int cupo, int cover, String fotoPrincipal) {
        this.id = id;
        this.nombre_evento = nombre_evento;
        this.privacidad = privacidad;
        this.categoria_evento = categoria_evento;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.cover = cover;
        this.fotoPrincipal = fotoPrincipal;
    }

    public String getFotoPrincipal() {
        return fotoPrincipal;
    }

    public void setImagen(String imagen) {
        this.fotoPrincipal= fotoPrincipal;
    }

    public int getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(int privacidad) {
        this.privacidad = privacidad;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public String getFecha() {
        return fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCategoria_evento() {
        return categoria_evento;
    }

    public void setCategoria_evento(int categoria_evento) {
        this.categoria_evento = categoria_evento;
    }
}
