package com.ekr.jularis.ui.login

import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.login.DataLogin
import com.ekr.jularis.data.response.ResponseLogin
import com.ekr.jularis.networking.ApiService
import com.ekr.jularis.utils.SessionManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(val view: LoginContract.View) : LoginContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
    }

    override fun doLogin(username: String, password: String) {
        view.onLoading(true)
        ApiService.endpoint.signIn(username, password)
            .enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    view.onLoading(false)
                    when{
                        response.isSuccessful->{
                            val responseLogin: ResponseLogin? = response.body()
                            if (responseLogin!!.status) {
                                view.showMessage(responseLogin.message)
                                view.onResult(responseLogin)
                            }
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

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage(t.message.toString())
                }

            })
    }

    override fun setPrefs(sessionManager: SessionManager, dataLogin: DataLogin) {
        sessionManager.prefIsLogin = true
        sessionManager.prefAlamat = dataLogin.address
        sessionManager.prefFullname = dataLogin.full_name
        sessionManager.prefUsername = dataLogin.username
        sessionManager.prefEmail = dataLogin.email
        sessionManager.prefNohp = dataLogin.no_telp
        sessionManager.prefToken = "Bearer "+ dataLogin.token
        sessionManager.prefRole = dataLogin.role
        sessionManager.prefFoto = dataLogin.photo
        sessionManager.prefFcm = dataLogin.firebase_token
    }
}