package com.ekr.jularis.ui.paymentall

import android.util.Log
import com.ekr.jularis.data.payment.*
import com.ekr.jularis.data.response.ResponseCart
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

class PaymentAllPresenter(val view: PaymentAllContract.View) : PaymentAllContract.Presenter {
    init {
        view.initListener()
        view.radioSelected()
        view.onLoading(false)
        view.loadingFoto(false)
        view.loadingChangeAddress(false)
    }

    override fun getDataPayment(token: String) {
        view.onLoading(true)
        ApiService.endpoint.getdataPaymentAll(token)
            .enqueue(object : Callback<ResponseGetDataPayment> {
                override fun onResponse(
                    call: Call<ResponseGetDataPayment>,
                    response: Response<ResponseGetDataPayment>
                ) {
                    view.onLoading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGetDataPayment? = response.body()
                            result?.let { view.onResultDataPayment(it, result.data) }
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(responseGlobal.message)
                        }
                        else -> {
                            view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGetDataPayment>, t: Throwable) {
                    view.onLoading(false)
                    Log.e("Cek", "onFailure: ${t.stackTrace}  , ${t.localizedMessage}" )
                    view.showMessage(t.stackTraceToString())
                }
            })
    }

    override fun changeAddressPayment(token: String, alamat: DataAlamat) {
        view.loadingChangeAddress(true)
        ApiService.endpoint.changeAddressPaymentAll(token, alamat)
            .enqueue(object : Callback<ResponseGetDataPayment> {
                override fun onResponse(
                    call: Call<ResponseGetDataPayment>,
                    response: Response<ResponseGetDataPayment>
                ) {
                    view.loadingChangeAddress(false)
                    when {
                        response.isSuccessful -> {
                            view.resultChangeAddress(true)
                            val result: ResponseGetDataPayment? = response.body()
                            result?.let { view.onResultDataPayment(it, result.data) }
                            view.showMessage("Ganti Alamat Berhasil")
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.resultChangeAddress(false)
                            view.showMessage(responseGlobal.message)
                        }
                        else -> {
                            view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGetDataPayment>, t: Throwable) {
                    view.loadingChangeAddress(false)
                    view.resultChangeAddress(false)
//                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                    view.showMessage(t.stackTraceToString())
                    Log.e("Ceking", "onFailure: ${t.stackTrace} dan : ${t.localizedMessage}", )
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
                            val result: String = response.body()!!.data.transaction_photo_id
                            view.onResultUploadPhoto(result)
                        }
                        response.code() != 200 -> {
                            val result: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.string(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(result.message)

                        }
                    }
                }

                override fun onFailure(call: Call<ResponsePhotopayment>, t: Throwable) {
                    view.loadingFoto(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")

                }
            })
    }

    override fun postDataPayment(token: String, datapostPayment: DatapostPayment2) {
        view.loadingFoto(true)
        ApiService.endpoint.doPaymentAll(token, datapostPayment)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    response: Response<ResponseGlobal>
                ) {
                    view.loadingFoto(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGlobal? = response.body()
                            if (result!!.status) {
                                view.showMessage(result.message)
                                view.resultPayment(result.status)
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

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.loadingFoto(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }
            })
    }

}