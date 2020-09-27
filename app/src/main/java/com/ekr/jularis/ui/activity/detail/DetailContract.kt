package com.ekr.jularis.ui.activity.detail

import android.content.Context

interface DetailContract {

    interface Presenter {
        fun doAddCart(token: String, product_id: String, quantity: Int)
        fun doBuy(context: Context,token: String, product_id: String, quantity: Int)
        fun doCalculatePlus(int: Int)
        fun doCalculateMinus(int: Int)
    }

    interface View {
        fun initListener()
        fun showMessage(message: String)
        fun onLoading(loading: Boolean)
        fun resultCounter(int: Int)
        fun actionButton()
    }
}