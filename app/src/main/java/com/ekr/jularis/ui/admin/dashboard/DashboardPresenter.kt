package com.ekr.jularis.ui.admin.dashboard

import com.ekr.jularis.data.response.ResponseBalanced
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseSellingtoday
import com.ekr.jularis.data.response.ResponseTopselling
import com.ekr.jularis.networking.ApiService
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardPresenter(val view: DashboardContract.View) : DashboardContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
//        view.nextLoading(false)
//        view.emptyTemplate(false)
    }

    override fun getTotalAmount(token: String) {
        ApiService.endpoint.getTotalAmount(token).enqueue(object : Callback<ResponseBalanced> {
            override fun onResponse(
                call: Call<ResponseBalanced>,
                response: Response<ResponseBalanced>
            ) {
                when {
                    response.isSuccessful -> {
                        val responseBalanced: ResponseBalanced? = response.body()
                        view.resultTotalAmount(responseBalanced!!.totalAmount)
                    }
                    response.code() != 200 -> {
                        response.body()?.message?.let { view.showMessage(it) }
                        view.showMessage(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBalanced>, t: Throwable) {
                view.showMessage("Something went wrong")
            }
        })
    }

    override fun getTopSelling(token: String) {
        ApiService.endpoint.getTopSelling(token).enqueue(object : Callback<ResponseTopselling> {
            override fun onResponse(
                call: Call<ResponseTopselling>,
                response: Response<ResponseTopselling>
            ) {
                when {
                    response.isSuccessful -> {
                        val responseTopselling: ResponseTopselling? = response.body()
                        responseTopselling?.let { view.resultTopSelling(it) }
                    }
                    response.code() != 200 -> {
                        val responseGlobal: ResponseGlobal? = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ResponseGlobal::class.java
                        )
                        responseGlobal?.message?.let { view.showMessage(it) }
                    }
                    else -> view.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseTopselling>, t: Throwable) {
                view.showMessage("Something went wrong")
            }

        })
    }

    override fun getSellingToday(token: String) {
        view.onLoading(true)
        ApiService.endpoint.getSellingToday(token).enqueue(object : Callback<ResponseSellingtoday> {
            override fun onResponse(
                call: Call<ResponseSellingtoday>,
                response: Response<ResponseSellingtoday>
            ) {
                view.onLoading(false)
                when {
                    response.isSuccessful -> {
                        val responseSellingToday: ResponseSellingtoday? = response.body()
                        responseSellingToday?.let { view.resultSellingToday(it) }
                    }
                    response.code() != 200 -> {
                        val responseGlobal: ResponseGlobal? = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ResponseGlobal::class.java
                        )
                        responseGlobal?.message?.let { view.showMessage(it) }
                    }
                    else -> view.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseSellingtoday>, t: Throwable) {
                view.onLoading(false)
                view.showMessage("Something went wrong")
            }
        })
    }

}