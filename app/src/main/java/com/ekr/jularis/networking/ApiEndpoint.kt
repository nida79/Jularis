package com.ekr.jularis.networking

import com.ekr.jularis.data.GlobalResponse
import com.ekr.jularis.data.login.ResponseLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiEndpoint {
    @FormUrlEncoded
    @POST("login")
    fun signIn(
        @Field("email") username : String,
        @Field("password") password : String
    ):Call<ResponseLogin>

    @FormUrlEncoded
    @POST("register")
    fun signUp(
        @Field("username") username: String,
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("full_name") full_name:String,
        @Field("no_telp") no_telp:String,
        @Field("address") address: String
    ):Call<GlobalResponse>

}