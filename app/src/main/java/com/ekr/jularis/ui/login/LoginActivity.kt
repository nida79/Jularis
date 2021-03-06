package com.ekr.jularis.ui.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ekr.jularis.R
import com.ekr.jularis.data.login.LoginGoogle
import com.ekr.jularis.data.response.ResponseLogin
import com.ekr.jularis.ui.MainActivity
import com.ekr.jularis.ui.MainActivity2
import com.ekr.jularis.ui.MainActivity3
import com.ekr.jularis.ui.register.RegisterActivity
import com.ekr.jularis.ui.reset.ResetPasswordActivity
import com.ekr.jularis.utils.Config
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.SessionManager
import com.ekr.jularis.utils.Validator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var loginPresenter: LoginPresenter
    private lateinit var sessionManager: SessionManager
    private var terima: String? = null
    private var admin: String? = null
    private var RC_SIGN_IN = 17
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginGoogle: LoginGoogle
    private lateinit var dialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dialog = DialogHelper.globalLoading(this)
        loginPresenter = LoginPresenter(this)
        sessionManager = SessionManager(this)
        terima = intent.getStringExtra("logout").toString()
        admin = intent.getStringExtra("admin").toString()

    }

    override fun initListener() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        sign_in_button.setOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
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

    override fun onLoadingGoogle(loading: Boolean) {
        when(loading){
            true->dialog.show()
            false->dialog.dismiss()
        }
    }

    override fun onResult(responseLogin: ResponseLogin) {
        responseLogin.data?.let { loginPresenter.setPrefs(sessionManager, it) }
        when {
            sessionManager.prefRole == "admin" -> {
                FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)
                startActivity(Intent(this, MainActivity2::class.java))
                finishAffinity()
                finish()
            }
            sessionManager.prefRole == "employee" -> {
                FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)
                startActivity(Intent(this, MainActivity3::class.java))
                finishAffinity()
                finish()
            }
            terima.equals("logout") -> {
                finish()
            }
            terima.equals("register") -> {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
                finish()
            }
            else -> {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
                finish()

            }
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
            val email = account.email.toString()
            val name = account.displayName.toString()
            loginGoogle = LoginGoogle(email, name)
            loginPresenter.doLoginGoogle(loginGoogle)
        } catch (e: ApiException) {
            Log.e("Error Google Login", "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun onBackPressed() {
        if (admin.equals("admin")) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
            finish()
        } else {
            finish()
        }
    }

}