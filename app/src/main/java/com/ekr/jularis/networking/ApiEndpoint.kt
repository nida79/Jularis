package com.ekr.jularis.networking

import com.ekr.jularis.data.response.ResponseCart
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseLogin
import com.ekr.jularis.data.response.ResponseProduct
import retrofit2.Call
import retrofit2.http.*


interface ApiEndpoint {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("login")
    fun signIn(
        @Field("email") username: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("register")
    fun signUp(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("full_name") full_name: String,
        @Field("no_telp") no_telp: String,
        @Field("address") address: String
    ): Call<ResponseGlobal>

    @Headers("Accept: application/json")
    @GET("product")
    fun getProduct(
        @Query("page") page: Int?,
        @Query("q") q: String?,
        @Query("start_price") start_price: String?,
        @Query("end_price") end_price: String?
    ): Call<ResponseProduct>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("checkout")
    fun addCart(
        @Header("Authorization") token: String,
        @Field("product_id") product_id: String,
        @Field("quantity") quantity: Int
    ): Call<ResponseGlobal>

    @Headers("Accept: application/json")
    @GET("checkout")
    fun getCartlist(
        @Header("Authorization") token: String): Call<ResponseCart>
}