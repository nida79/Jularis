package com.ekr.jularis.ui.admin.employee.manage

import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.data.employee.EmployeeAdd
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeManagePresenter(val view: EmployeeManageContract.View) :
    EmployeeManageContract.Presenter {
    init {
        view.initListener()
        view.loading(false)
    }

    override fun doAddKaryawan(token: String, employeeAdd: EmployeeAdd) {
        view.loading(true)
        ApiService.endpoint.doAddEmployee(token, employeeAdd)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    response: Response<ResponseGlobal>
                ) {
                    view.loading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGlobal? = response.body()
                            result?.let { view.resultManage(it) }
                            result?.message?.let { view.showMessage(it) }
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

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.loading(false)
                    view.showMessage(t.message.toString())
                }
            })
    }

    override fun doUpdateKaryawan(token: String, idKrw: String, employeeAdd: EmployeeAdd) {
        view.loading(true)
        ApiService.endpoint.doUpdateEmployee(token, idKrw, employeeAdd)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    response: Response<ResponseGlobal>
                ) {
                    view.loading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseGlobal? = response.body()
                            result?.let { view.resultManage(it) }
                            result?.message?.let { view.showMessage(it) }
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

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.loading(false)
                    view.showMessage(t.message.toString())
                }
            })
    }

    override fun doDeleteKaryawan(token: String, idKrw: String) {
        view.loading(true)
        ApiService.endpoint.doDeleteEmployee(token,idKrw).enqueue(object : Callback<ResponseGlobal>{
            override fun onResponse(
                call: Call<ResponseGlobal>,
                response: Response<ResponseGlobal>
            ) {
                view.loading(false)
                when {
                    response.isSuccessful -> {
                        val result: ResponseGlobal? = response.body()
                        result?.let { view.resultManage(it) }
                        result?.message?.let { view.showMessage(it) }
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

            override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                view.loading(false)
                view.showMessage(t.message.toString())
            }
        })
    }
}