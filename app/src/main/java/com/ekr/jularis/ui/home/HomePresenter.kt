package com.ekr.jularis.ui.home

import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseProduct
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomePresenter(val view: HomeContract.View) : HomeContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
        view.onNextLoading(false)
        view.actionAdapterClick()

    }
    override fun getProduct(page: Int?,seachKey: String?,start_price: String?,end_price: String?) {
        view.onLoading(true)
        ApiService.endpoint.getProduct(page, seachKey, start_price, end_price)
            .enqueue(object : Callback<ResponseProduct> {
                override fun onResponse(
                    call: Call<ResponseProduct>,
                    response: Response<ResponseProduct>
                ) {
                    view.onLoading(false)
                    when {
                        response.isSuccessful -> {
                            val responseProduct: ResponseProduct? = response.body()
                            responseProduct?.let { view.onResultProduct(it) }
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

                override fun onFailure(call: Call<ResponseProduct>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage(t.message.toString())
                }

            })
    }

    override fun getNextProduct(page: Int) {
        view.onNextLoading(true)
        ApiService.endpoint.getProduct(page, null, null, null)
            .enqueue(object : Callback<ResponseProduct> {
                override fun onResponse(
                    call: Call<ResponseProduct>,
                    response: Response<ResponseProduct>
                ) {
                    view.onNextLoading(false)
                    if (response.isSuccessful) {
                        val responseProduct: ResponseProduct? = response.body()
                        responseProduct?.let { view.onResultNextPage(it) }
                    } else {
                        if (response.code() != 200) {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(responseGlobal.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseProduct>, t: Throwable) {
                    view.onNextLoading(false)
                    view.showMessage(t.message.toString())
                }

            })
    }

    override fun doBuy(token: String, product_id: String, quantity: Int) {
        view.onNextLoading(true)
        ApiService.endpoint.addCart(token, product_id, quantity)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    responseGlobal: Response<ResponseGlobal>
                ) {
                    view.onNextLoading(false)
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
                                view.resultBuy(result.message, result.data!!)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.onNextLoading(false)
                    view.showMessage(t.message.toString())
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