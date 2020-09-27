package com.ekr.jularis.ui.fragment.cart

import com.ekr.jularis.data.response.ResponseCart

interface CartContract {
    interface Presenter {
        fun getCartlist(token: String)
        fun doCalculatePlus(int: Int)
        fun doCalculateMinus(int: Int)
    }

    interface View {
        fun initListener()
        fun showMessage(message: String)
        fun onLoading(boolean: Boolean)
        fun onResult(responseCart: ResponseCart)
        fun actionCheckbox()
        fun resultCounter(int: Int)
    }
}