package com.ekr.jularis.ui.setting

import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingPresenter (val view: SettingContract.View):SettingContract.Presenter{
    init {
        view.initListener()
        view.loadingLogout(false)
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
                    }
                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.showMessage(t.stackTraceToString())
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
                view.showMessage(t.stackTraceToString())
            }
        })
    }

}