package com.ekr.jularis.ui.admin.product.mange

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import com.facebook.stetho.Stetho as Stetho1


class ProductManagePresenter(val view: ProductManageContract.View, val context: Context) :
    ProductManageContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
    }

    override fun doUpdateProduct(
        token: String,
        product_id: String,
        name: String,
        price: String,
        description: String,
        category: String,
        quantity: String,
        product_discont_quantity: String?,
        product_discont_present: String?,
        photo_product: List<File>?
    ) {
        view.onLoading(true)
        AndroidNetworking.initialize(context)
        AndroidNetworking.upload("http://103.55.36.171:8001/v1/product/update/{product_id}")
            .addHeaders("Authorization", token)
            .addPathParameter("product_id", product_id)
            .addMultipartFileList("product_picture[]", photo_product)
            .addMultipartParameter("name", name)
            .addMultipartParameter("price", price)
            .addMultipartParameter("ongkir", "0")
            .addMultipartParameter("category", category)
            .addMultipartParameter("product_discont_quantity", product_discont_quantity)
            .addMultipartParameter("product_discont_present", product_discont_present)
            .addMultipartParameter("description", description)
            .addMultipartParameter("quantity", quantity)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    view.onLoading(false)
                    view.showMessage(response!!.getString("message"))
                    view.resultUpdateProduct(response.getBoolean("status"))
                }

                override fun onError(anError: ANError?) {
                    view.onLoading(false)
                    view.showMessage(anError!!.message.toString())
                }

            })
    }

    override fun doDeleteProduct(token: String, product_id: String) {
        view.onLoading(true)
        ApiService.endpoint.doDeleteProduct(token, product_id)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    response: Response<ResponseGlobal>
                ) {
                    view.onLoading(false)
                    when {
                        response.isSuccessful -> {
                            view.showMessage(response.body()!!.message)
                            view.resultUpdateProduct(response.body()!!.status)
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(responseGlobal.message)
                        }
                        else -> {
                            view.showMessage(response.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage("Terjadi Kesalahan Silahkan Muat Ulang")
                }

            })
    }

    override fun doAddProduct(
        token: String,
        name: String,
        price: String,
        description: String,
        category: String,
        quantity: String,
        product_discont_quantity: String?,
        product_discont_present: String?,
        photo_product: List<File>
    ) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient =
            OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder().addHeader("Accept", "application/json").build()
                chain.proceed(request)
            }.build()
        view.onLoading(true)
        val ongkir = "0"
        AndroidNetworking.initialize(context, httpClient)
        AndroidNetworking.upload("http://103.55.36.171:8001/v1/product")
            .addHeaders("Authorization", token)
            .addMultipartFileList("product_picture[]", photo_product)
            .addMultipartParameter("name", name)
            .addMultipartParameter("price", price)
            .addMultipartParameter("category", category)
            .addMultipartParameter("ongkir", ongkir)
            .addMultipartParameter("description", description)
            .addMultipartParameter("product_discont_quantity", product_discont_quantity)
            .addMultipartParameter("product_discont_present", product_discont_present)
            .addMultipartParameter("quantity", quantity)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    view.onLoading(false)
                    view.showMessage(response!!.getString("message"))
                    view.resultUpdateProduct(response.getBoolean("status"))
                }

                override fun onError(anError: ANError?) {
                    view.onLoading(false)
                    view.showMessage(anError!!.message.toString())
                }

            })
    }

}