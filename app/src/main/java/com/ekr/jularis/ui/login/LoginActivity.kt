package com.ekr.jularis.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ekr.jularis.R
import com.ekr.jularis.data.response.ResponseLogin
import com.ekr.jularis.ui.MainActivity
import com.ekr.jularis.ui.detail.DetailActivity
import com.ekr.jularis.ui.register.RegisterActivity
import com.ekr.jularis.ui.reset.ResetPasswordActivity
import com.ekr.jularis.utils.SessionManager
import com.ekr.jularis.utils.Validator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var loginPresenter: LoginPresenter
    private lateinit var sessionManager: SessionManager
    private var terima: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginPresenter = LoginPresenter(this)
        sessionManager = SessionManager(this)
        terima = intent.getStringExtra("logout").toString()

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
            when {
                tie_username_login.text.toString() == "" -> {
                    tie_username_login.error = "Username atau Email Tidak Boleh Kosong"
                    tie_username_login.requestFocus()
                }
                tie_pswd_login.text.toString() == "" -> {
                    tie_pswd_login.error = "Password Tidak Boleh Kosong"
                    tie_pswd_login.requestFocus()
                }
                !Validator.validatePassword(tie_pswd_login.text.toString()) -> {
                    tie_pswd_login.error = "Input Password Minimal 8 Karakter"
                    tie_pswd_login.requestFocus()
                }
                else -> {
                    loginPresenter.doLogin(
                        tie_username_login.text.toString(),
                        tie_pswd_login.text.toString()
                    )
                }
            }
        }
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                spin_kit_login.visibility = View.VISIBLE
                btn_login.visibility = View.GONE
                btn_login_to_register.visibility = View.GONE
            }
            false -> {
                spin_kit_login.visibility = View.GONE
                btn_login.visibility = View.VISIBLE
                btn_login_to_register.visibility = View.VISIBLE
            }
        }
    }

    override fun onResult(responseLogin: ResponseLogin) {
        responseLogin.data?.let { loginPresenter.setPrefs(sessionManager, it) }
        if (terima.equals("logout")) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
            finish()
        } else {
            finish()
        }

    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    }

    override fun onBackPressed() {
        finish()
    }

}