package com.ekr.jularis.ui.admin.employee.aktifitas

import com.ekr.jularis.data.response.ResponseKegiatanEmployee
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeKegiatanPresenter(val view: EmployeeKegiatanContract.View) :
    EmployeeKegiatanContract.Presenter {
    init {
        view.firstLoading(false)
        view.nextLoading(false)
        view.initListener()
    }

    override fun getKegiatanEmployee(token: String, page: Int?, q: String?) {
        view.firstLoading(true)
        ApiService.endpoint.getEmployeeActivity(token, page, q)
            .enqueue(object : Callback<ResponseKegiatanEmployee> {
                override fun onResponse(
                    call: Call<ResponseKegiatanEmployee>,
                    response: Response<ResponseKegiatanEmployee>
                ) {
                    view.firstLoading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseKegiatanEmployee? = response.body()
                            result?.let { view.resultKegiatanEmployee(it) }
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(responseGlobal.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseKegiatanEmployee>, t: Throwable) {

                    view.firstLoading(false)
                }
            })
    }

    override fun getNextPage(token: String, page: Int) {
        view.nextLoading(true)
        ApiService.endpoint.getEmployeeActivity(token, page, null)
            .enqueue(object : Callback<ResponseKegiatanEmployee> {
                override fun onResponse(
                    call: Call<ResponseKegiatanEmployee>,
                    response: Response<ResponseKegiatanEmployee>
                ) {
                    view.nextLoading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseKegiatanEmployee? = response.body()
                            result?.let { view.resultNextPage(it) }
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(responseGlobal.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseKegiatanEmployee>, t: Throwable) {
                    view.showMessage("Something Went Wrong")
                    view.nextLoading(false)
                }
            })
    }

}