package com.ekr.jularis.ui.history.detail

import android.app.Activity
import com.ekr.jularis.data.histori.HistoriIUpdate
import com.ekr.jularis.data.histori.ReportDaily
import com.ekr.jularis.data.response.ResponseGetReport
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseLogin
import com.ekr.jularis.networking.ApiService
import com.ekr.jularis.utils.DownloadUtils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionPresenter(val view: TransactionDetailContract.View) :
    TransactionDetailContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
    }

    override fun doUpdate(
        token: String,
        transaction_id: String,
        historiIUpdate: HistoriIUpdate
    ) {
        view.onLoading(true)
        ApiService.endpoint.doUpdateTransaction(token, transaction_id, historiIUpdate)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    response: Response<ResponseGlobal>
                ) {
                    view.onLoading(false)
                    when {

                        response.isSuccessful -> {
                            view.showMessage(response.body()!!.message)
                            view.onResultUpdate(response.body()!!.status)
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(responseGlobal.message)
                        }
                        else -> {
                            view.showMessage(response.message())
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage("Terjadi kesalahan, silahkan coba kembali")
                }

            })
    }

    override fun downloadReport(activity: Activity, url: String) {
        DownloadUtils.downloadLaporan(activity, url)
    }

    override fun doGetUrl(token: String, reportDaily: ReportDaily) {
        view.onLoading(true)
        ApiService.endpoint.postReportDaily(token, reportDaily)
            .enqueue(object : Callback<ResponseGetReport> {
                override fun onResponse(
                    call: Call<ResponseGetReport>,
                    response: Response<ResponseGetReport>
                ) {
                    view.onLoading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGetReport? = response.body()
                            result?.let { view.onResultDwonload(it) }
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal? = Gson().fromJson(
                                response.errorBody()?.string(),
                                ResponseGlobal::class.java
                            )
                            responseGlobal?.message?.let { view.showMessage(it) }
                        }
                        else -> view.showMessage(response.message())
                    }
                }

                override fun onFailure(call: Call<ResponseGetReport>, t: Throwable) {
                    view.onLoading(false)
                    view.showMessage("Terjadi Kesalahan Silahkan Ulangi Kembali !")
                }
            })
    }


}