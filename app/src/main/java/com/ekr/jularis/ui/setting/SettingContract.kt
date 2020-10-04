package com.ekr.jularis.ui.setting

interface SettingContract{
    interface Presenter{
        fun doLogout(token:String)
        fun changePassword(token: String,old_password:String,new_password:String)
    }
    interface View{
        fun initListener()
        fun loadingLogout(loading:Boolean)
        fun resultChangePassword(boolean: Boolean)
        fun resultLogout(status: Boolean)
        fun showMessage(message: String)
    }
}