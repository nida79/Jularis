package com.ekr.jularis.ui.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ekr.jularis.R
import com.ekr.jularis.ui.activity.home.MainActivity
import com.ekr.jularis.ui.activity.register.RegisterActivity
import com.ekr.jularis.ui.activity.reset.ResetPasswordActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var loginPresenter: LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginPresenter = LoginPresenter(this)
    }

    override fun initListener() {
        tv_login_to_reset.setOnClickListener {
            startActivity(Intent(applicationContext, ResetPasswordActivity::class.java))
        }
        tv_login_to_reset2.setOnClickListener {
            startActivity(Intent(applicationContext, ResetPasswordActivity::class.java))
        }
        btn_login_to_register.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
        btn_login.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }


    override fun onLoading(loading: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showMessage(message: String) {
        TODO("Not yet implemented")
    }
}