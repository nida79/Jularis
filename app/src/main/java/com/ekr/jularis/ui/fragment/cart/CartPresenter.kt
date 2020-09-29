package com.ekr.jularis.ui.fragment.cart

import com.ekr.jularis.data.response.ResponseCart
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.networking.ApiService
import com.ekr.jularis.utils.PembayaranHelper
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartPresenter(val view: CartContract.View) : CartContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
        view.loadingHorizontal(false)
    }

    override fun getCartlist(token: String) {
        view.onLoading(true)
        ApiService.endpoint.getCartlist(token).enqueue(object : Callback<ResponseCart> {
            override fun onResponse(call: Call<ResponseCart>, response: Response<ResponseCart>) {
                view.onLoading(false)
                when{
                    response.isSuccessful->{
                        val responseCart : ResponseCart = response.body()!!
                        responseCart.let { view.onResult(it) }
                    }
                    response.code() !=200->{
                        val responseGlobal: ResponseGlobal = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        if (responseGlobal.message == "Keranjang kosong"){
                            view.showEmptyCart(responseGlobal.message)
                        }else{
                            view.showToast(responseGlobal.message)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ResponseCart>, t: Throwable) {
                view.onLoading(false)
                view.showToast(t.message.toString())

            }

        })
    }
    override fun getCartUpdate(token: String) {
       view.loadingHorizontal(true)
        ApiService.endpoint.getCartlist(token).enqueue(object : Callback<ResponseCart> {
            override fun onResponse(call: Call<ResponseCart>, response: Response<ResponseCart>) {
                view.loadingHorizontal(false)
                when{
                    response.isSuccessful->{
                        val responseCart : ResponseCart = response.body()!!
                        responseCart.let { view.onResult(it) }
                    }
                    response.code() !=200->{
                        val responseGlobal: ResponseGlobal = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        if (responseGlobal.message == "Keranjang kosong"){
                            view.showEmptyCart(responseGlobal.message)
                        }else{
                            view.showToast(responseGlobal.message)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ResponseCart>, t: Throwable) {
                view.loadingHorizontal(false)
                view.showToast(t.message.toString())

            }

        })
    }

    override fun doCalculatePlus(token: String, checked: String, productId: String,qty: Int) {
        view.loadingHorizontal(true)
        val datapost = PembayaranHelper.addCart(checked,productId,qty)
        ApiService.endpoint.doCalculation(token,datapost).enqueue(object : Callback<ResponseGlobal>{
            override fun onResponse(
                call: Call<ResponseGlobal>,
                response: Response<ResponseGlobal>
            ) {
                view.loadingHorizontal(false)
                when {
                    response.isSuccessful -> {
                        val result: ResponseGlobal? = response.body()
                        if (result!!.status) {
                            view.resultUpdate(result.message)
                        }
                    }
                    response.code() != 200 -> {
                        val result: ResponseGlobal = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        view.showToast(result.message)

                    }
                }
            }

            override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                view.loadingHorizontal(false)
                view.showToast(t.message.toString())

            }

        })
    }

    override fun doCalculateMinus(token: String, checked: String, productId: String, qty: Int) {
        view.loadingHorizontal(true)
        val datapost = PembayaranHelper.minusCart(checked,productId,qty)
        ApiService.endpoint.doCalculation(token,datapost).enqueue(object : Callback<ResponseGlobal>{
            override fun onResponse(
                call: Call<ResponseGlobal>,
                response: Response<ResponseGlobal>
            ) {
                view.loadingHorizontal(false)
                when {
                    response.isSuccessful -> {
                        val result: ResponseGlobal? = response.body()
                        if (result!!.status) {
                            view.resultUpdate(result.message)
                        }
                    }
                    response.code() != 200 -> {
                        val result: ResponseGlobal = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        view.showToast(result.message)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                view.loadingHorizontal(false)
                view.showToast(t.message.toString())
            }

        })
    }

    override fun doCheckBoxClicked(token: String, checked: String, productId: String, qty: Int) {
        view.loadingHorizontal(true)
        val datapost = PembayaranHelper.statisCart(checked,productId,qty)
        ApiService.endpoint.doCalculation(token,datapost).enqueue(object : Callback<ResponseGlobal>{
            override fun onResponse(
                call: Call<ResponseGlobal>,
                response: Response<ResponseGlobal>
            ) {
                view.loadingHorizontal(false)
                when {
                    response.isSuccessful -> {
                        val result: ResponseGlobal? = response.body()
                        if (result!!.status) {

                            view.resultUpdate(result.message)
                        }
                    }
                    response.code() != 200 -> {
                        val result: ResponseGlobal = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        view.showToast(result.message)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                view.loadingHorizontal(false)
                view.showToast(t.message.toString())
            }

        })
    }

    override fun doDeleteItem(token: String) {
        view.loadingHorizontal(true)
        ApiService.endpoint.deleteItemCart(token).enqueue(object : Callback<ResponseGlobal>{
            override fun onResponse(
                call: Call<ResponseGlobal>,
                response: Response<ResponseGlobal>
            ) {
                view.loadingHorizontal(false)
                when {
                    response.isSuccessful -> {
                        val result: ResponseGlobal? = response.body()
                        if (result!!.status) {
                            view.showToast(result.message)
                            view.resultUpdate(result.message)
                        }
                    }
                    response.code() != 200 -> {
                        val result: ResponseGlobal = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        view.showToast(result.message)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                view.loadingHorizontal(false)
                view.showToast(t.message.toString())
            }
        })
    }
}