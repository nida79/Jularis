package com.ekr.jularis.ui.history.prosess

import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseNewHistori
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProsesPresenter(val view: ProsesContract.View) : ProsesContract.Presenter {
    init {
        view.initListener()
        view.firstLoading(false)
        view.nextLoading(false)
    }

    override fun getHistoriFirst(token: String, page: Int?, q: String?) {
        view.firstLoading(true)
        ApiService.endpoint.getHistoryProgress(token, page, q)
            .enqueue(object : Callback<ResponseNewHistori> {
                override fun onResponse(
                    call: Call<ResponseNewHistori>,
                    response: Response<ResponseNewHistori>
                ) {
                    view.firstLoading(false)
                    when {
                        response.isSuccessful -> {
                            val responseHistory: ResponseNewHistori? = response.body()
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
                    }
                }

                override fun onFailure(call: Call<ResponseNewHistori>, t: Throwable) {
                    view.firstLoading(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }
            })
    }

    override fun getHistoriNext(token: String, page: Int?, q: String?) {
        view.nextLoading(true)
        ApiService.endpoint.getHistoryProgress(token, page, q)
            .enqueue(object : Callback<ResponseNewHistori> {
                override fun onResponse(
                    call: Call<ResponseNewHistori>,
                    response: Response<ResponseNewHistori>
                ) {
                    view.nextLoading(false)
                    when {
                        response.isSuccessful -> {
                            val responseHistory: ResponseNewHistori? = response.body()
                            responseHistory?.let { view.resultNextRequest(it) }
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

                override fun onFailure(call: Call<ResponseNewHistori>, t: Throwable) {
                    view.nextLoading(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }
            })
    }

}