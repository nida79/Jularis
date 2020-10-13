package com.ekr.jularis.ui.detail

import android.content.Context
import android.content.Intent
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.networking.ApiService
import com.ekr.jularis.ui.payment.PaymentActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPresenter(val view: DetailContract.View) : DetailContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
        view.actionButton()
    }

    override fun doAddCart(token: String, product_id: String, quantity: Int) {
        view.onLoading(true)
        ApiService.endpoint.addCart(token, product_id, quantity)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    responseGlobal: Response<ResponseGlobal>
                ) {
                    view.onLoading(false)
                    if (responseGlobal.code() != 200) {
                        val result: ResponseGlobal = Gson().fromJson(
                            responseGlobal.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        view.showMessage(result.message)

                    } else {
                        if (responseGlobal.isSuccessful) {
                            val result: ResponseGlobal? = responseGlobal.body()
                            view.showMessage(result!!.message)

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }

            })
    }

    override fun doBuy(context: Context, token: String, product_id: String, quantity: Int) {
        view.onLoading(true)
        ApiService.endpoint.addCart(token, product_id, quantity)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    responseGlobal: Response<ResponseGlobal>
                ) {
                    view.onLoading(false)
                    when {
                        responseGlobal.code() != 200 -> {
                            val result: ResponseGlobal = Gson().fromJson(
                                responseGlobal.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(result.message)
                        }
                        responseGlobal.isSuccessful -> {
                            val result: ResponseGlobal = responseGlobal.body()!!
                            if (result.status) {
                                val intent = Intent(context, PaymentActivity::class.java)
                                intent.putExtra("pd_id", result.data!!.productId)
                                context.startActivity(intent)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }

            })
    }

    override fun doCalculatePlus(int: Int) {
         var hasil = int
         hasil++
         view.resultCounter(hasil)
    }

    override fun doCalculateMinus(int: Int) {
        var hasil = int
        hasil--
        view.resultCounter(hasil)
    }
}