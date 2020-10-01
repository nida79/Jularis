package com.ekr.jularis.ui.login

import com.ekr.jularis.data.login.DataLogin
import com.ekr.jularis.data.response.ResponseLogin
import com.ekr.jularis.utils.SessionManager

interface LoginContract {

    interface Presenter {
        fun doLogin(username: String, password: String)
        fun setPrefs(sessionManager: SessionManager,dataLogin: DataLogin)
    }

    interface View {
        fun initListener()
        fun onLoading(loading: Boolean)
        fun onResult(responseLogin: ResponseLogin)
        fun showMessage(message: String)

    }
}