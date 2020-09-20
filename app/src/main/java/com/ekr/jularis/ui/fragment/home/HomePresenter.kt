package com.ekr.jularis.ui.fragment.home

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.ekr.jularis.data.GlobalResponse
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.product.ResponseProduct
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomePresenter(val view: HomeContract.View) : HomeContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)

    }
    override fun getProduct(page: Int) {
        view.onLoading(true)
        ApiService.endpoint.getProduct(page).enqueue(object : Callback<ResponseProduct> {
            override fun onResponse(
                call: Call<ResponseProduct>,
                response: Response<ResponseProduct>
            ) {
                view.onLoading(false)
                if (response.isSuccessful) {
                    val responseProduct: ResponseProduct? = response.body()
                    responseProduct?.let { view.onResultProduct(it) }
                } else {
                    if (response.code() != 200) {
                        val globalResponse: GlobalResponse = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            GlobalResponse::class.java
                        )
                        view.showMessage(globalResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseProduct>, t: Throwable) {
                view.onLoading(false)
                view.showMessage(t.message.toString())
            }

        })
    }

}