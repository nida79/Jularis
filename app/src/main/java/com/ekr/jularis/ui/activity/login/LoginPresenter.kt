package com.ekr.jularis.ui.activity.login

import com.ekr.jularis.data.login.DataLogin
import com.ekr.jularis.data.login.ResponseLogin
import com.ekr.jularis.networking.ApiService
import com.ekr.jularis.utils.SessionManager
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
                    if (response.isSuccessful) {
                        val responseLogin: ResponseLogin? = response.body()
                        if (responseLogin!!.status) {
                            view.showMessage(responseLogin.message)
                            view.onResult(responseLogin)
                        }
                    } else {
                        view.showMessage(response.message().toString())
                    }

                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    view.onLoading(false)
                }

            })
    }

    override fun setPrefs(sessionManager: SessionManager, dataLogin: DataLogin) {
        sessionManager.prefIsLogin = true
        sessionManager.prefFullname = dataLogin.full_name
        sessionManager.prefUsername = dataLogin.username
        sessionManager.prefEmail = dataLogin.email
        sessionManager.prefNohp = dataLogin.no_telp
        sessionManager.prefToken = dataLogin.token
        sessionManager.prefRole = dataLogin.role
        sessionManager.prefFoto = dataLogin.photo
    }
}