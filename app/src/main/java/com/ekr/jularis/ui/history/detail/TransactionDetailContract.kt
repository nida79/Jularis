package com.ekr.jularis.ui.history.detail

import com.ekr.jularis.data.histori.HistoriIUpdate

interface TransactionDetailContract {
    interface Presenter {
        fun doUpdate(token:String,transaction_id:String,historiIUpdate: HistoriIUpdate)
    }

    interface View {
        fun initListener()
        fun onResultUpdate(berhasil:Boolean)
        fun onLoading(loading:Boolean)
        fun showMessage(message:String)
    }
}