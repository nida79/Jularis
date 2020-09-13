package com.ekr.jularis.ui.activity.login

interface LoginContract {

    interface View {
        fun initListener()
        fun onLoading(loading: Boolean)
//        fun onResult(responseLogin: ResponseLogin)
        fun showMessage(message: String)
    }
}