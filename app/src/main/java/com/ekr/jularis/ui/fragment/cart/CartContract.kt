package com.ekr.jularis.ui.fragment.cart

import com.ekr.jularis.data.response.ResponseCart

interface CartContract {
    interface Presenter {
        fun getCartlist(token: String)
    }

    interface View {
        fun initListener()
        fun showMessage(message: String)
        fun onLoading(boolean: Boolean)
        fun onResult(responseCart: ResponseCart)
    }
}