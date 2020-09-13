package com.ekr.jularis.ui.activity.register

import com.ekr.jularis.data.GlobalResponse
import com.ekr.jularis.networking.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPresnter(val view: RegisterContract.View) : RegisterContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
    }

    override fun doRegister(
        username:String,
        email: String,
        password: String,
        full_name: String,
        no_telp: String,
        address: String)
    {
        view.onLoading(true)
        ApiService.endpoint.signUp(username,email, password, no_telp, full_name, address)
            .enqueue(object : Callback<GlobalResponse> {
                override fun onResponse(
                    call: Call<GlobalResponse>,
                    response: Response<GlobalResponse>) {
                    view.onLoading(false)
                    if (response.isSuccessful){
                       val globalResponse : GlobalResponse?= response.body()
                        view.showMessage(globalResponse!!.message)
                        if (globalResponse.status){
                            view.onResult(globalResponse)
                        }
                    }
                }
                override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                   view.onLoading(false)
                }

            })
    }

}