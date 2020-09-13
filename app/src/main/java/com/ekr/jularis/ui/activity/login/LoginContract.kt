package com.ekr.jularis.ui.activity.login

import android.content.Context
import com.ekr.jularis.data.login.DataLogin
import com.ekr.jularis.data.login.ResponseLogin
import com.ekr.jularis.utils.SessionManager
import org.json.JSONObject

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