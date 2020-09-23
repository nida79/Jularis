package com.ekr.jularis.ui.activity.detail

import android.content.Context
import android.content.Intent
import com.ekr.jularis.data.response.GlobalResponse
import com.ekr.jularis.networking.ApiService
import com.ekr.jularis.ui.activity.CheckoutActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPresenter(val view: DetailContract.View) : DetailContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
    }

    override fun doAddCart(token: String, product_id: String, quantity: Int) {
        view.onLoading(true)
        ApiService.endpoint.addCart(token, product_id, quantity)
            .enqueue(object : Callback<GlobalResponse> {
                override fun onResponse(
                    call: Call<GlobalResponse>,
                    response: Response<GlobalResponse>
                ) {
                    view.onLoading(false)
                    if (response.code() != 200) {
                        val globalResponse: GlobalResponse = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            GlobalResponse::class.java
                        )
                        view.showMessage(globalResponse.message)

                    } else {
                        if (response.isSuccessful) {
                            val globalResponse: GlobalResponse? = response.body()
                            view.showMessage(globalResponse!!.message)

                        }
                    }
                }

                override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage(t.message.toString())
                }

            })
    }

    override fun doBuy(context: Context, token: String, product_id: String, quantity: Int) {
        view.onLoading(true)
        ApiService.endpoint.addCart(token, product_id, quantity)
            .enqueue(object : Callback<GlobalResponse> {
                override fun onResponse(
                    call: Call<GlobalResponse>,
                    response: Response<GlobalResponse>
                ) {
                    view.onLoading(false)
                    when {
                        response.code() != 200 -> {
                            val globalResponse: GlobalResponse = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                GlobalResponse::class.java
                            )
                            view.showMessage(globalResponse.message)
                        }
                        response.isSuccessful -> {
                            val globalResponse: GlobalResponse = response.body()!!
                            if (globalResponse.status) {
                                val intent = Intent(context, CheckoutActivity::class.java)
                                context.startActivity(intent)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage(t.message.toString())
                }

            })
    }

}