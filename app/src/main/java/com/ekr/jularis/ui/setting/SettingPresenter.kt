package com.ekr.jularis.ui.setting

import android.util.Log
import com.ekr.jularis.data.product.OngkirData
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseOngkir
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingPresenter (val view: SettingContract.View):SettingContract.Presenter{
    init {
        view.initListener()
        view.loadingLogout(false)
        view.loadingOngkir(false)
    }
    override fun doLogout(token: String) {
        view.loadingLogout(true)
        ApiService.endpoint.logoutAccount(token,"BEBAS")
            .enqueue(object : Callback<ResponseGlobal>{
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    response: Response<ResponseGlobal>
                ) {
                    view.loadingLogout(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGlobal? = response.body()
                            if (result!!.status) {
                                    view.resultLogout(result.status)
                                view.showMessage(result.message)
                            }
                        }
                        response.code() != 200 -> {
                            val result: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.resultLogout(result.status)
                            view.showMessage(result.message)
                        }
                        else->{
                            view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }
            })
    }

    override fun changePassword(token: String, old_password: String, new_password: String) {
        ApiService.endpoint.doChangePassword(token,old_password, new_password).enqueue(object : Callback<ResponseGlobal>{
            override fun onResponse(
                call: Call<ResponseGlobal>,
                response: Response<ResponseGlobal>
            ) {

                when {
                    response.isSuccessful -> {
                        val result: ResponseGlobal? = response.body()
                        if (result!!.status) {

                            view.resultChangePassword(result.status)
                            view.showMessage(result.message)
                        }
                    }
                    response.code() != 200 -> {
                        val result: ResponseGlobal = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        view.showMessage(result.message)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
            }
        })
    }

    override fun getOngkir(token: String) {
        view.loadingOngkir(true)
        ApiService.endpoint.getOngkir(token).enqueue(object : Callback<ResponseOngkir> {
            override fun onResponse(
                call: Call<ResponseOngkir>,
                response: Response<ResponseOngkir>
            ) {
                view.loadingOngkir(false)
                when {
                    response.isSuccessful -> {
                        val responseOngkir: ResponseOngkir? = response.body()
                        responseOngkir?.let { view.resultGetOngkir(it) }
                    }
                    response.code() != 200 -> {
                        val responseOngkir: ResponseOngkir? = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseOngkir::class.java
                        )
                        responseOngkir?.message?.let { view.showMessage(it) }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseOngkir>, t: Throwable) {
                view.loadingOngkir(false)
                Log.e("Kenapa", "onFailureOnkir: ${t.cause}" )
            }
        })
    }

    override fun setOngkir(token: String, data: OngkirData) {
        ApiService.endpoint.setOngkir(token,data).enqueue(object : Callback<ResponseOngkir> {
            override fun onResponse(
                call: Call<ResponseOngkir>,
                response: Response<ResponseOngkir>
            ) {
                when{
                    response.isSuccessful->{
                        val result : ResponseOngkir? = response.body()
                        view.resultOngkir(result!!.status)
                        view.showMessage(result.message)
                    }
                    response.code()!=200->{
                        val result: ResponseOngkir = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseOngkir::class.java
                        )
                        view.showMessage(result.message)
                        view.resultOngkir(result.status)
                    }
                }

            }

            override fun onFailure(call: Call<ResponseOngkir>, t: Throwable) {
                t.message?.let { view.showMessage(it) }
                view.resultOngkir(false)
            }
        })
    }

}