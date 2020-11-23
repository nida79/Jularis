package com.ekr.jularis.ui.admin.dashboard

import android.app.Activity
import android.content.IntentSender
import com.ekr.jularis.data.dashboard.PostDownload
import com.ekr.jularis.data.response.*
import com.ekr.jularis.networking.ApiService
import com.ekr.jularis.utils.DownloadUtils
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardPresenter(val view: DashboardContract.View) : DashboardContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
        view.onDownloadProgress(false)
    }

    override fun getTotalAmount(token: String) {
        ApiService.endpoint.getTotalAmount(token).enqueue(object : Callback<ResponseBalanced> {
            override fun onResponse(
                call: Call<ResponseBalanced>,
                response: Response<ResponseBalanced>
            ) {
                when {
                    response.isSuccessful -> {
                        val responseBalanced: ResponseBalanced? = response.body()
                        view.resultTotalAmount(responseBalanced!!.totalAmount)
                    }
                    response.code() != 200 -> {
                        response.body()?.message?.let { view.showMessage(it) }
                        view.showMessage(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBalanced>, t: Throwable) {
                view.showMessage("Something went wrong")
            }
        })
    }

    override fun getTopSelling(token: String) {
        ApiService.endpoint.getTopSelling(token).enqueue(object : Callback<ResponseTopselling> {
            override fun onResponse(
                call: Call<ResponseTopselling>,
                response: Response<ResponseTopselling>
            ) {
                when {
                    response.isSuccessful -> {
                        val responseTopselling: ResponseTopselling? = response.body()
                        responseTopselling?.let { view.resultTopSelling(it) }
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

            override fun onFailure(call: Call<ResponseTopselling>, t: Throwable) {
                view.showMessage("Something went wrong")
            }

        })
    }

    override fun getSellingToday(token: String) {
        view.onLoading(true)
        ApiService.endpoint.getSellingToday(token).enqueue(object : Callback<ResponseSellingtoday> {
            override fun onResponse(
                call: Call<ResponseSellingtoday>,
                response: Response<ResponseSellingtoday>
            ) {
                view.onLoading(false)
                when {
                    response.isSuccessful -> {
                        val responseSellingToday: ResponseSellingtoday? = response.body()
                        responseSellingToday?.let { view.resultSellingToday(it) }
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

            override fun onFailure(call: Call<ResponseSellingtoday>, t: Throwable) {
                view.onLoading(false)
                view.showMessage("Something went wrong")
            }
        })
    }

    override fun dogetReport(token: String, postDownload: PostDownload) {
        view.onDownloadProgress(true)
        ApiService.endpoint.getReport(token, postDownload).enqueue(object :
            Callback<ResponseGetReport> {
            override fun onResponse(
                call: Call<ResponseGetReport>,
                response: Response<ResponseGetReport>
            ) {
                view.onDownloadProgress(false)
                when {
                    response.isSuccessful -> {
                        val result: ResponseGetReport? = response.body()
                        result?.let { view.resultGetUrl(it) }
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
                view.onDownloadProgress(false)
                view.showMessage("Something went wrong!")
            }
        })
    }

    override fun downloadReport(activity: Activity, url: String) {
        DownloadUtils.downloadLaporan(activity, url)

    }

    override fun doCheckUpdate(activity: Activity, requestCode: Int) {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        activity,
                        requestCode
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun doResumeUpdate(activity: Activity, requestCode: Int) {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        activity,
                        requestCode
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

}