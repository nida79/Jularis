package com.ekr.jularis.ui.admin.dashboard

import android.app.Activity
import android.content.Context
import com.ekr.jularis.data.dashboard.PostDownload
import com.ekr.jularis.data.dashboard.ProductSelling
import com.ekr.jularis.data.response.ResponseGetReport
import com.ekr.jularis.data.response.ResponseSellingtoday
import com.ekr.jularis.data.response.ResponseTopselling
import okhttp3.ResponseBody

interface DashboardContract {
    interface Presenter{
        fun getTotalAmount(token:String)
        fun getTopSelling (token: String)
        fun getSellingToday (token: String)
        fun dogetReport(token: String,postDownload: PostDownload)
        fun downloadReport(activity: Activity,url:String)
        fun doCheckUpdate(activity: Activity,requestCode:Int)
        fun doResumeUpdate(activity: Activity,requestCode:Int)
    }
    interface View{
        fun onLoading(loading:Boolean)
        fun resultGetUrl(responseGetReport: ResponseGetReport)
        fun initListener()
        fun showMessage(message:String)
        fun onDownloadProgress(loading: Boolean)
        fun resultTotalAmount(uang:Int)
        fun resultTopSelling(responseTopselling: ResponseTopselling)
        fun resultSellingToday(responseSellingtoday: ResponseSellingtoday)

    }
}