package com.ekr.jularis.ui.fragment.home

import com.ekr.jularis.data.response.ResponseProduct

interface HomeContract {
    interface Presenter {
        fun getProduct(page: Int?, seachKey: String?, start_price: String?, end_price: String?)
        fun getNextProduct(page: Int)
    }

    interface View {
        fun initListener()
        fun onLoading(loading: Boolean)
        fun onNextLoading(nextLoading: Boolean)
        fun onResultProduct(responseProduct: ResponseProduct)
        fun onResultNextPage(responseProduct: ResponseProduct)
        fun showMessage(message: String)
    }
}