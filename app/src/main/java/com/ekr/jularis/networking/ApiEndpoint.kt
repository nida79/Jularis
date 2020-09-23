package com.ekr.jularis.networking

import com.ekr.jularis.data.response.GlobalResponse
import com.ekr.jularis.data.response.ResponseLogin
import com.ekr.jularis.data.response.ResponseProduct
import retrofit2.Call
import retrofit2.http.*


interface ApiEndpoint {
    @FormUrlEncoded
    @POST("login")
    fun signIn(
        @Field("email") username: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("register")
    fun signUp(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("full_name") full_name: String,
        @Field("no_telp") no_telp: String,
        @Field("address") address: String
    ): Call<GlobalResponse>

    @GET("product")
    fun getProduct(
        @Query("page") page: Int
    ): Call<ResponseProduct>

    @FormUrlEncoded
    @POST("checkout")
    fun addCart(
        @Header("Authorization") token: String,
        @Field("product_id") product_id: String,
        @Field("quantity") quantity: Int
    ): Call<GlobalResponse>
}