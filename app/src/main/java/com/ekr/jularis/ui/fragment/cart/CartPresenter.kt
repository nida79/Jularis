package com.ekr.jularis.ui.fragment.cart

import com.ekr.jularis.data.response.ResponseCart
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseLogin
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartPresenter(val view: CartContract.View) : CartContract.Presenter {
    init {
        view.initListener()
        view.actionCheckbox()
        view.onLoading(false)
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
                        view.showMessage(responseGlobal.message)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseCart>, t: Throwable) {
                view.onLoading(false)
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