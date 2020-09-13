package com.ekr.jularis.ui.activity.login

class LoginPresenter(val view: LoginContract.View) {
    init {
        view.initListener()
    }
}