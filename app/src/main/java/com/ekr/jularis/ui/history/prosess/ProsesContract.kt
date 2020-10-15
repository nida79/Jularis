package com.ekr.jularis.ui.history.prosess

import com.ekr.jularis.data.response.ResponseHistory
import com.ekr.jularis.data.response.ResponseNewHistori

interface ProsesContract{
    interface Presenter{
        fun getHistoriFirst(token:String,page:Int?,q:String?)
        fun getHistoriNext(token:String,page:Int?,q:String?)
    }
    interface View{
        fun initListener()
        fun firstLoading(firstLoading:Boolean)
        fun nextLoading(nextLoading:Boolean)
        fun showMessage(message:String)
        fun showEmptyCart(message: String)
        fun resultFirstRequest(responseHistory: ResponseNewHistori)
        fun resultNextRequest(responseHistory: ResponseNewHistori)
    }
}