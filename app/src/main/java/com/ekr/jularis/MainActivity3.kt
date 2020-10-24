package com.ekr.jularis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ekr.jularis.ui.MainActivity
import com.ekr.jularis.ui.MainActivity2
import com.ekr.jularis.ui.admin.dashboard.DashboardFragment
import com.ekr.jularis.ui.admin.product.ProductFragment
import com.ekr.jularis.ui.history.TransactionFragment
import com.ekr.jularis.ui.setting.SettingFragment
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : AppCompatActivity() {
    var exit: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val sessionManager = SessionManager(this)
        if (sessionManager.prefIsLogin) {
            when (sessionManager.prefRole) {
                "user" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                    finish()
                }
                "admin" -> {
                    startActivity(Intent(this, MainActivity2::class.java))
                    finishAffinity()
                    finish()
                }
            }

        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_krw,
                ProductFragment()
            ).commit()
        }
        animatedBottomBarKrw.setItemSelected(R.id.navigation_product_krw)
        fragmentChanged()

    }

    private fun fragmentChanged() {

        animatedBottomBarKrw.setOnItemSelectedListener { id ->
            var selectedFragment: Fragment? = null
            when (id) {
                R.id.navigation_product_krw -> selectedFragment = ProductFragment()
                R.id.navigation_trans_krw -> selectedFragment = TransactionFragment()
                R.id.navigation_setting_krw -> selectedFragment = SettingFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container_krw,
                    it,
                ).commit()

            }
            return@setOnItemSelectedListener
        }
    }

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - exit) > 2000) {
            Toast.makeText(applicationContext, "Tekan Sekali Lagi Untuk Keluar", Toast.LENGTH_SHORT)
                .show()
            exit = System.currentTimeMillis()
        } else {
            finishAffinity()
            finish()
        }
    }
}