package com.ekr.jularis.ui.history.detail

import android.app.Activity
import com.ekr.jularis.data.histori.HistoriIUpdate
import com.ekr.jularis.data.histori.ReportDaily
import com.ekr.jularis.data.response.ResponseGetReport

interface TransactionDetailContract {
    interface Presenter {
        fun doUpdate(token:String,transaction_id:String,historiIUpdate: HistoriIUpdate)
        fun downloadReport(activity: Activity, url:String)
        fun doGetUrl(token: String,reportDaily: ReportDaily)
    }

    interface View {
        fun initListener()
        fun onResultUpdate(berhasil:Boolean)
        fun onResultDwonload(responseGetReport: ResponseGetReport)
        fun onLoading(loading:Boolean)
        fun showMessage(message:String)
    }
}