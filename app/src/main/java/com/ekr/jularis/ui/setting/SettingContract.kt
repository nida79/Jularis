package com.ekr.jularis.ui.setting

interface SettingContract{
    interface Presenter{
        fun doLogout(token:String)
    }
    interface View{
        fun initListener()
        fun loadingLogout(loading:Boolean)
        fun resultLogout(status: Boolean)
        fun showMessage(message: String)
    }
}