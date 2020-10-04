package com.ekr.jularis.ui.home

import com.ekr.jularis.data.cart.postcheckout.Data
import com.ekr.jularis.data.response.ResponseProduct

interface HomeContract {
    interface Presenter {
        fun getProduct(page: Int?, seachKey: String?, start_price: String?, end_price: String?)
        fun getNextProduct(page: Int)
        fun doBuy(token: String, product_id: String, quantity: Int)
        fun doCalculatePlus(int: Int)
        fun doCalculateMinus(int: Int)
    }

    interface View {
        fun initListener()
        fun onLoading(loading: Boolean)
        fun onNextLoading(nextLoading: Boolean)
        fun onResultProduct(responseProduct: ResponseProduct)
        fun onResultNextPage(responseProduct: ResponseProduct)
        fun showMessage(message: String)
        fun resultCounter(int: Int)
        fun resultBuy(message: String,data: Data)
        fun actionAdapterClick()
    }
}