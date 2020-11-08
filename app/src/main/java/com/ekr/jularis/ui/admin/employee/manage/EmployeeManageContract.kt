package com.ekr.jularis.ui.admin.employee.manage

import android.text.BoringLayout
import com.ekr.jularis.data.employee.EmployeeAdd
import com.ekr.jularis.data.response.ResponseGlobal

interface EmployeeManageContract {

    interface Presenter {
        fun doAddKaryawan(token: String, employeeAdd: EmployeeAdd)
        fun doUpdateKaryawan(token: String, idKrw: String, employeeAdd: EmployeeAdd)
        fun doDeleteKaryawan(token: String,idKrw: String)
    }

    interface View {
        fun initListener()
        fun loading(loading:Boolean)
        fun resultManage(responseGlobal: ResponseGlobal)
        fun showMessage(message:String)

    }
}