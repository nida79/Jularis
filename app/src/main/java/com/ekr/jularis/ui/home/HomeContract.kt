package com.ekr.jularis.ui.home

import com.ekr.jularis.data.cart.postcheckout.Data
import com.ekr.jularis.data.login.DataLogin
import com.ekr.jularis.data.profile.DataProfile
import com.ekr.jularis.data.response.ResponseProduct
import com.ekr.jularis.data.response.ResponseUpdateProfile
import com.ekr.jularis.utils.SessionManager

interface HomeContract {
    interface Presenter {
        fun getProduct(page: Int?, seachKey: String?, start_price: String?, end_price: String?)
        fun getNextProduct(page: Int)
        fun doBuy(token: String, product_id: String, quantity: Int)
        fun doUpdateFcm(token: String,fcmToken:String)
        fun setPrefs(sessionManager: SessionManager, dataProfile: DataProfile)
        fun doCalculatePlus(int: Int)
        fun doCalculateMinus(int: Int)
    }

    interface View {
        fun initListener()
        fun onLoading(loading: Boolean)
        fun fcmLoading(fcmLoading:Boolean)
        fun resultFcm(responseUpdateProfile: ResponseUpdateProfile)
        fun onNextLoading(nextLoading: Boolean)
        fun onResultProduct(responseProduct: ResponseProduct)
        fun onResultNextPage(responseProduct: ResponseProduct)
        fun showMessage(message: String)
        fun resultCounter(int: Int)
        fun resultBuy(message: String,data: Data)
        fun actionAdapterClick()
    }
}