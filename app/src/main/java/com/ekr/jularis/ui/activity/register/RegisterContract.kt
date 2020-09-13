package com.ekr.jularis.ui.activity.register

import com.ekr.jularis.data.GlobalResponse
import com.ekr.jularis.data.login.ResponseLogin

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
        fun onResult(globalResponse: GlobalResponse)
        fun showMessage(message: String)
    }
}