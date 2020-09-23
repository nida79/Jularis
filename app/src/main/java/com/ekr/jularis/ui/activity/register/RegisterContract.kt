package com.ekr.jularis.ui.activity.register

import com.ekr.jularis.data.response.GlobalResponse

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