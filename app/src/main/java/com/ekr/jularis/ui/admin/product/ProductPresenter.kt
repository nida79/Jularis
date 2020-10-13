package com.ekr.jularis.ui.admin.product

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseProduct
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProductPresenter (val view:ProductContract.View,val context: Context):ProductContract.Presenter{
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
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
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
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }

            })
    }


}