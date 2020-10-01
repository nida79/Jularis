package com.ekr.jularis.ui.register

import com.ekr.jularis.data.response.ResponseGlobal

interface RegisterContract {

    interface Presenter {
        fun doRegister(
            username:String,
            email: String,
            password: String,
            full_name: String,
            no_telp: String,
            address: String
        )
    }

    interface View {
        fun initListener()
        fun onLoading(loading: Boolean)
        fun onResult(responseGlobal: ResponseGlobal)
        fun showMessage(message: String)
    }
}