package com.ekr.jularis.ui.payment

import android.util.Log
import com.ekr.jularis.data.payment.DatapostPayment
import com.ekr.jularis.data.response.ResponseGetDataPayment
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponsePhotopayment
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PaymentPresenter(val view: PaymentContract.View) : PaymentContract.Presenter {

    init {
        view.initListener()
        view.onLoading(false)
        view.loadingFoto(false)
    }

    override fun getDataPayment(token: String, product_id: String) {
        view.onLoading(true)
        ApiService.endpoint.getdataPaymentOne(token, product_id)
            .enqueue(object : Callback<ResponseGetDataPayment> {
                override fun onResponse(
                    call: Call<ResponseGetDataPayment>,
                    response: Response<ResponseGetDataPayment>
                ) {
                    view.onLoading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGetDataPayment? = response.body()
                            result?.let { view.onResultDataPayment(it) }
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(responseGlobal.message)
                        }
                    }
                }


                override fun onFailure(call: Call<ResponseGetDataPayment>, t: Throwable) {
                    view.onLoading(false)
                    Log.e("ERORR", "onFailure: ${t.message}",)
                    view.showMessage(t.message.toString())
                }

            })

    }

    override fun uploadFoto(token: String, photo: File) {
        view.loadingFoto(true)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), photo)
        val multipartBody: MultipartBody.Part? = MultipartBody.Part.createFormData(
            "photo",
            photo.name, requestBody
        )
        ApiService.endpoint.uploadPhotopayment(token, multipartBody!!)
            .enqueue(object : Callback<ResponsePhotopayment> {
                override fun onResponse(
                    call: Call<ResponsePhotopayment>,
                    response: Response<ResponsePhotopayment>
                ) {
                    view.loadingFoto(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponsePhotopayment? = response.body()
                            if (result!!.status) {
                                view.onResultUploadPhoto(result.data.transaction_photo_id)
                            }
                        }
                        response.code() != 200 -> {
                            val result: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(result.message)

                        }
                    }
                }

                override fun onFailure(call: Call<ResponsePhotopayment>, t: Throwable) {
                    view.loadingFoto(false)
                    view.showMessage(t.message.toString())

                }
            })
    }

    override fun postDataPayment(datapostPayment: DatapostPayment) {
        TODO("Not yet implemented")
    }

}