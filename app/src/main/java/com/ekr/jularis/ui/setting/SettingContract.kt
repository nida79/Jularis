package com.ekr.jularis.ui.setting

import com.ekr.jularis.data.product.OngkirData
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.data.response.ResponseOngkir

interface SettingContract{
    interface Presenter{
        fun doLogout(token:String)
        fun changePassword(token: String,old_password:String,new_password:String)
        fun getOngkir(token: String)
        fun setOngkir(token: String,data: OngkirData)
    }
    interface View{
        fun initListener()
        fun loadingLogout(loading:Boolean)
        fun resultChangePassword(boolean: Boolean)
        fun resultGetOngkir(responseOngkir: ResponseOngkir)
        fun loadingOngkir(loading:Boolean)
        fun resultOngkir(hasil:Boolean)
        fun resultLogout(status: Boolean)
        fun showMessage(message: String)
    }
}