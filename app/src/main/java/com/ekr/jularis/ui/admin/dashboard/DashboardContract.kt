package com.ekr.jularis.ui.admin.dashboard

import com.ekr.jularis.data.dashboard.ProductSelling
import com.ekr.jularis.data.response.ResponseSellingtoday
import com.ekr.jularis.data.response.ResponseTopselling

interface DashboardContract {
    interface Presenter{
        fun getTotalAmount(token:String)
        fun getTopSelling (token: String)
        fun getSellingToday (token: String)
    }
    interface View{
        fun onLoading(loading:Boolean)
//        fun nextLoading(loading:Boolean)
        fun initListener()
        fun showMessage(message:String)
//        fun emptyTemplate(show:Boolean)
        fun resultTotalAmount(uang:Int)
        fun resultTopSelling(responseTopselling: ResponseTopselling)
        fun resultSellingToday(responseSellingtoday: ResponseSellingtoday)

    }
}