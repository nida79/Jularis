package com.ekr.jularis.ui.history.selesai

import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseHistori
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelesaiPresenter(val view: SelesaiContract.View) : SelesaiContract.Presenter {
    init {
        view.initListener()
        view.firstLoading(false)
        view.nextLoading(false)
    }

    override fun getHistoriFirst(token: String, page: Int?, q: String?) {
        view.firstLoading(true)
        ApiService.endpoint.getHistoryComplete(token, page, q)
            .enqueue(object : Callback<ResponseHistori> {
                override fun onResponse(
                    call: Call<ResponseHistori>,
                    response: Response<ResponseHistori>
                ) {
                    view.firstLoading(false)
                    when {
                        response.isSuccessful -> {
                            val responseHistory: ResponseHistori? = response.body()
                            responseHistory?.let { view.resultFirstRequest(it) }
                            responseHistory?.message?.let { view.showEmptyCart(it) }
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showEmptyCart(responseGlobal.message)
                        }
                        else -> {
                            view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseHistori>, t: Throwable) {
                    view.firstLoading(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }
            })
    }

    override fun getHistoriNext(token: String, page: Int?, q: String?) {
        view.nextLoading(true)
        ApiService.endpoint.getHistoryComplete(token, page, q)
            .enqueue(object : Callback<ResponseHistori> {
                override fun onResponse(
                    call: Call<ResponseHistori>,
                    response: Response<ResponseHistori>
                ) {
                    view.nextLoading(false)
                    when {
                        response.isSuccessful -> {
                            val responseHistory: ResponseHistori? = response.body()
                            responseHistory?.let { view.resultNextRequest(it) }
                            responseHistory?.message?.let { view.showEmptyCart(it) }
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

                override fun onFailure(call: Call<ResponseHistori>, t: Throwable) {
                    view.nextLoading(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }
            })
    }


}