package com.ekr.jularis.networking

import com.ekr.jularis.data.cart.postcheckout.DataPOST
import com.ekr.jularis.data.histori.HistoriIUpdate
import com.ekr.jularis.data.payment.DatapostPayment
import com.ekr.jularis.data.payment.DatapostPayment2
import com.ekr.jularis.data.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File


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
    ): Call<ResponseGlobal>

    @Multipart
    @POST("product/update/{product_id}")
    fun updateProduct(
        @Header("Authorization") token: String,
        @Path("product_id") product_id: String,
        @Part("name") name: String,
        @Part("price") price: Int,
        @Part("description") description: String,
        @Part("category") category: String,
        @Part("quantity") quantity: Int,
        @Part product_picture: List<MultipartBody.Part>?
    ): Call<ResponseGlobal>

    @GET("product")
    fun getProduct(
        @Query("page") page: Int?,
        @Query("q") q: String?,
        @Query("start_price") start_price: String?,
        @Query("end_price") end_price: String?
    ): Call<ResponseProduct>

    @FormUrlEncoded
    @POST("checkout")
    fun addCart(
        @Header("Authorization") token: String,
        @Field("product_id") product_id: String,
        @Field("quantity") quantity: Int
    ): Call<ResponseGlobal>

    @GET("checkout")
    fun getCartlist(
        @Header("Authorization") token: String
    ): Call<ResponseCart>

    @POST("checkout/calculation")
    fun doCalculation(
        @Header("Authorization") token: String,
        @Body data: DataPOST
    ): Call<ResponseGlobal>

    @DELETE("checkout-delete-bulk")
    fun deleteItemCart(
        @Header("Authorization") token: String
    ): Call<ResponseGlobal>

    @GET("transaction/process/{product_id}")
    fun getdataPaymentOne(
        @Header("Authorization") token: String,
        @Path("product_id") product_id: String
    ): Call<ResponseGetDataPayment>

    @GET("transaction/process/bulk")
    fun getdataPaymentAll(@Header("Authorization") token: String): Call<ResponseGetDataPayment>

    @Multipart
    @POST("transaction/photo")
    fun uploadPhotopayment(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part
    ): Call<ResponsePhotopayment>

    @FormUrlEncoded
    @POST("logout")
    fun logoutAccount(
        @Header("Authorization") token: String,
        @Field("logout") bebas: String
    ): Call<ResponseGlobal>

    @FormUrlEncoded
    @POST("checkout/all")
    fun checkAll(
        @Header("Authorization") token: String,
        @Field("checked") checked: Int
    ): Call<ResponseGlobal>

    @POST("transaction")
    fun doPayment(
        @Header("Authorization") token: String,
        @Body datapostPayment: DatapostPayment
    ): Call<ResponseGlobal>

    @POST("transaction")
    fun doPaymentAll(
        @Header("Authorization") token: String,
        @Body datapostPayment: DatapostPayment2
    ): Call<ResponseGlobal>

    @FormUrlEncoded
    @POST("profile/change-password")
    fun doChangePassword(
        @Header("Authorization") token: String,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String
    ): Call<ResponseGlobal>

    @FormUrlEncoded
    @POST("profile/update")
    fun doUpdateProfile(
        @Header("Authorization") token: String,
        @Field("full_name") full_name: String,
        @Field("no_telp") no_telp: String,
        @Field("address") address: String
    ): Call<ResponseUpdateProfile>

    @Multipart
    @POST("profile/change-photo")
    fun doChangePhotoProfile(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part
    ): Call<ResponseGlobal>

    @GET("profile")
    fun getProfile(@Header("Authorization") token: String): Call<ResponseGetProfile>

    @GET("transaction?transaction_state=Selesai")
    fun getHistoryComplete(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("q") q: String?
    ): Call<ResponseHistory>

    @GET("transaction?transaction_state_not=Selesai")
    fun getHistoryProgress(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("q") q: String?
    ): Call<ResponseHistory>

    @DELETE("product/{product_id}")
    fun doDeleteProduct(
        @Header("Authorization") token: String,
        @Path("product_id") product_id: String
    ): Call<ResponseGlobal>

    @PUT("transaction/{transaction_product_id}")
    fun doUpdateTransaction(
        @Header("Authorization") token: String,
        @Path("transaction_product_id") transaction_product_id: String,
        @Body update:HistoriIUpdate
    ): Call<ResponseGlobal>
}