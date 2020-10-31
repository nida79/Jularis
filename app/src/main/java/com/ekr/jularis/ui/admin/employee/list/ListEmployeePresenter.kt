package com.ekr.jularis.ui.admin.employee.list

import com.ekr.jularis.data.response.ResponseGetEmployee
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListEmployeePresenter(val view: ListEmployeeContract.View) : ListEmployeeContract.Presenter {
    init {
        view.initListener()
        view.firstLoading(false)
        view.nextLoading(false)
    }

    override fun getEmployee(token: String, page: Int?, q: String?) {
        view.firstLoading(true)
        ApiService.endpoint.getEmployee(token, page, q)
            .enqueue(object : Callback<ResponseGetEmployee> {
                override fun onResponse(
                    call: Call<ResponseGetEmployee>,
                    response: Response<ResponseGetEmployee>
                ) {
                    view.firstLoading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGetEmployee? = response.body()
                            result?.let { view.resultGetEmployee(it) }
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

                override fun onFailure(call: Call<ResponseGetEmployee>, t: Throwable) {

                    view.firstLoading(false)
                }
            })
    }

    override fun getNextPage(token: String, page: Int) {
        view.nextLoading(true)
        ApiService.endpoint.getEmployee(token, page, null)
            .enqueue(object : Callback<ResponseGetEmployee> {
                override fun onResponse(
                    call: Call<ResponseGetEmployee>,
                    response: Response<ResponseGetEmployee>
                ) {
                    view.nextLoading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGetEmployee? = response.body()
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

                override fun onFailure(call: Call<ResponseGetEmployee>, t: Throwable) {
                    view.showMessage("Something Went Wrong")
                    view.nextLoading(false)
                }
            })
    }


}