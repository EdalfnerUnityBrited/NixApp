package com.example.nixapp.modelotablas;


import androidx.annotation.NonNull;

import com.example.nixapp.common.MyApp;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyApp.class)
public class UsuarioToken extends BaseModel {

    @PrimaryKey
    public // at least one primary key required
            String id;

    @Column
    public
    String access_token;
    @Column
    public
    String token_type;
    @Column
    public
    String expires_at;

    @NonNull
    @Override
    public String toString() {
        return token_type + " " + access_token;
    }
}
