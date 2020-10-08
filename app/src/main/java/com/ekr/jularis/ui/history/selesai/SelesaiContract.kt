package com.ekr.jularis.ui.history.selesai

import com.ekr.jularis.data.response.ResponseHistory

interface SelesaiContract {
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
        fun resultFirstRequest(responseHistory: ResponseHistory)
        fun resultNextRequest(responseHistory: ResponseHistory)
    }

}