package com.ekr.jularis.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ekr.jularis.R
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.ui.login.LoginActivity
import com.ekr.jularis.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View {
    private lateinit var registerPresnter: RegisterPresnter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerPresnter = RegisterPresnter(this)
    }

    override fun initListener() {
        tv_regis_to_login.setOnClickListener { finish() }
        tv_regis_to_login2.setOnClickListener { finish() }
        btn_regis_submit.setOnClickListener {
            when {
                tie_regis_username.text.toString().isEmpty() -> {
                    tie_regis_username.error = "Username Tidak Boleh Kosong"
                    tie_regis_username.requestFocus()
                }
                tie_regis_alamat.text.toString().isEmpty() -> {
                    tie_regis_alamat.error = "Alamat Tidak Boleh Kosong"
                    tie_regis_alamat.requestFocus()
                }
                tie_regis_nohp.text.toString().isEmpty() -> {
                    tie_regis_nohp.error = "No Hp / Telp Tidak Boleh Kosong"
                    tie_regis_nohp.requestFocus()
                }
                tie_regis_nama.text.toString().isEmpty() -> {
                    tie_regis_nama.error = "Nama Lengkap Tidak Boleh Kosong"
                    tie_regis_nama.requestFocus()
                }
                tie_regis_pswd.text.toString().isEmpty() -> {
                    tie_regis_pswd.error = "Password Tidak Boleh Kosong"
                    tie_regis_pswd.requestFocus()
                }
                !Validator.validatePassword(tie_regis_pswd.text.toString()) -> {
                    tie_pswd_login.error = "Input Password Minimal 8 Karakter"
                    tie_pswd_login.requestFocus()
                }
                !Validator.validateEmail(tie_regis_email.text.toString()) -> {
                    tie_regis_email.error = "Format Email Tidak Tepat"
                    tie_regis_email.requestFocus()
                }
                tie_regis_email.text.toString().isEmpty() -> {
                    tie_regis_email.error = "Email Tidak Boleh Kosong"
                    tie_regis_email.requestFocus()
                }
                else -> {
                    registerPresnter.doRegister(
                        tie_regis_username.text.toString().toLowerCase(Locale.ROOT).trim(),
                        tie_regis_email.text.toString().toLowerCase(Locale.ROOT).trim(),
                        tie_regis_pswd.text.toString().trim(),
                        tie_regis_nama.text.toString(),
                        tie_regis_nohp.text.toString().trim(),
                        tie_regis_alamat.text.toString()
                    )
                }
            }
        }


    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                spin_kit_register.visibility = View.VISIBLE
                btn_regis_submit.visibility = View.GONE
                tv_regis_to_login.visibility = View.GONE
                tv_regis_to_login2.visibility = View.GONE

            }
            false -> {
                spin_kit_register.visibility = View.GONE
                btn_regis_submit.visibility = View.VISIBLE
                tv_regis_to_login.visibility = View.VISIBLE
                tv_regis_to_login2.visibility = View.VISIBLE

            }
        }
    }

    override fun onResult(responseGlobal: ResponseGlobal) {
      if (responseGlobal.status){
          val intent  = Intent(this,LoginActivity::class.java)
          intent.putExtra("logout","register")
          startActivity(intent)
          finishAffinity()
          finish()
      }
    }

    override fun showMessage(message: String) {
        Toasty.success(applicationContext,message,Toasty.LENGTH_SHORT).show()
    }
}