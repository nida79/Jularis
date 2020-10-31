package com.ekr.jularis.ui.admin.employee.list

import com.ekr.jularis.data.response.ResponseGetEmployee

interface ListEmployeeContract {
    interface Presenter {
        fun getEmployee(token: String,page: Int?,q: String?)
        fun getNextPage(token: String, page: Int)
    }

    interface View {
        fun initListener()
        fun firstLoading(loading: Boolean)
        fun nextLoading(nextLoading: Boolean)
        fun resultGetEmployee(responseGetEmployee: ResponseGetEmployee)
        fun resultNextPage(responseGetEmployee: ResponseGetEmployee)
        fun showMessage(message: String)
    }
}