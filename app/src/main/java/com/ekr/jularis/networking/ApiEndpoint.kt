package com.ekr.jularis.networking

import com.ekr.jularis.data.cart.postcheckout.DataPOST
import com.ekr.jularis.data.response.*
import okhttp3.MultipartBody
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
        @Header("Authorization") token: String
    ): Call<ResponseCart>

    @Headers("Accept: application/json")
    @POST("checkout/calculation")
    fun doCalculation(
        @Header("Authorization") token: String,
        @Body data: DataPOST
    ): Call<ResponseGlobal>

    @Headers("Accept: application/json")
    @DELETE("checkout-delete-bulk")
    fun deleteItemCart(
        @Header("Authorization") token: String
    ): Call<ResponseGlobal>

    @Headers("Accept: application/json")
    @GET("transaction/process/{product_id}")
    fun getdataPaymentOne(
        @Header("Authorization") token: String,
        @Path("product_id") product_id: String
    ): Call<ResponseGetDataPayment>

    @Headers("Accept: application/json")
    @GET("transaction/process/bulk")
    fun getdataPaymentAll(@Header("Authorization") token: String): Call<ResponseCart>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("profile/change-password")
    fun changePassword(
        @Header("Authorization") token: String,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String
    ): Call<ResponseGlobal>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("profile/update")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Field("username") username: String,
        @Field("full_name") full_name: String,
        @Field("email") email: String,
        @Field("no_telp") no_telp: String,
        @Field("address") addres: String,

        ): Call<ResponseGlobal>

    @Headers("Accept: application/json")
    @Multipart
    @POST("transaction/photo")
    fun uploadPhotopayment(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part
    ):Call<ResponsePhotopayment>

    @POST("logout")
    fun logoutAccount( @Header("Authorization") token: String):Call<ResponseGlobal>
}