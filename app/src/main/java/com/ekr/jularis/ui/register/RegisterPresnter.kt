package com.ekr.jularis.ui.register

import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPresnter(val view: RegisterContract.View) : RegisterContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
    }

    override fun doRegister(
        username: String,
        email: String,
        password: String,
        full_name: String,
        no_telp: String,
        address: String
    ) {
        view.onLoading(true)
        ApiService.endpoint.signUp(username, email, password, full_name, no_telp, address)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    responseGlobal: Response<ResponseGlobal>
                ) {
                    view.onLoading(false)
                    when {
                        responseGlobal.isSuccessful -> {
                            val result: ResponseGlobal? = responseGlobal.body()
                            if (result!!.status) {
                                view.onResult(result)
                                view.showMessage(result.message)
                            }
                        }
                        responseGlobal.code() != 200 -> {
                            val result: ResponseGlobal = Gson().fromJson(
                                responseGlobal.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(result.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.onLoading(false)
                }

            })
    }

}