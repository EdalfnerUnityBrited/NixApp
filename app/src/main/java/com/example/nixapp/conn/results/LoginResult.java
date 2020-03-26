package com.example.nixapp.conn.results;

import com.example.nixapp.DB.RequestUsuarios;
import com.example.nixapp.modelotablas.UsuarioToken;

public class LoginResult extends RequestResult {

    String access_token;
    String token_type;
    String expires_at;

    public RequestUsuarios usuario;

    public void procesarRespuesta(){
        UsuarioToken tokenUser= new UsuarioToken();
        tokenUser.id="1";
        tokenUser.access_token=access_token;
        tokenUser.token_type=token_type;
        tokenUser.expires_at=expires_at;

        tokenUser.save();

    }
}

