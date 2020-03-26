package com.example.nixapp.DB.controllers;

import com.example.nixapp.modelotablas.UsuarioToken;
import com.raizlabs.android.dbflow.sql.language.SQLite;

public class TokenController {

    public static UsuarioToken getToken() {

        return SQLite.select()
                .from(UsuarioToken.class)
                .querySingle();
    }

}
