package com.ekr.jularis.ui.admin.employee.aktifitas

import com.ekr.jularis.data.response.ResponseKegiatanEmployee

interface EmployeeKegiatanContract{
    interface Presenter{
        fun getKegiatanEmployee(token: String,page: Int?,q: String?)
        fun getNextPage(token: String, page: Int)
    }
    interface View{
        fun initListener()
        fun firstLoading(loading: Boolean)
        fun nextLoading(nextLoading: Boolean)
        fun resultKegiatanEmployee(responseKegiatanEmployee: ResponseKegiatanEmployee)
        fun resultNextPage(responseKegiatanEmployee: ResponseKegiatanEmployee)
        fun showMessage(message: String)
    }
}