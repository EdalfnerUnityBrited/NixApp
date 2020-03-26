package com.example.nixapp.conn;

import com.example.nixapp.DB.RequestUsuarios;
import com.example.nixapp.conn.results.LoginResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NixService {

    @POST("auth/signup")
    Call<ResponseBody> usuario(@Body RequestUsuarios usuarios);

    @POST("auth/login")
    Call<LoginResult> login(@Body RequestUsuarios usuarios);

    @GET("auth/user")
    Call<RequestUsuarios> getUser();

}
