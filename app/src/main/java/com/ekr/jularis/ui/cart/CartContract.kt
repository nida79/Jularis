package com.ekr.jularis.ui.cart

import com.ekr.jularis.data.response.ResponseCart

interface CartContract {
    interface Presenter {
        fun getCartlist(token: String)
        fun getCartUpdate(token: String)
        fun doCalculatePlus(token:String,checked:String,productId:String,qty: Int)
        fun doCalculateMinus(token:String,checked:String,productId:String,qty: Int)
        fun doCheckBoxClicked(token:String,checked:String,productId:String,qty: Int)
        fun doCheckAll(token: String,checked:Int)
        fun doDeleteItem(token: String)
    }

    interface View {
        fun initListener()
        fun showEmptyCart(message: String)
        fun onLoading(boolean: Boolean)
        fun showToast(message: String)
        fun loadingHorizontal(boolean: Boolean)
        fun onResult(responseCart: ResponseCart)
        fun resultUpdate(hasil:Boolean)
    }
}