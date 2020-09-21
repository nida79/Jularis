package com.ekr.jularis.ui.fragment.home
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.product.ResponseProduct

interface HomeContract {
    interface Presenter {
        fun getProduct(page: Int)
    }

    interface View {
        fun initListener()
        fun onRefreshLoading(loading: Boolean)
        fun onNextLoading(nextLoading:Boolean)
        fun onResultProduct(responseProduct: ResponseProduct)
        fun showMessage(message: String)
    }
}