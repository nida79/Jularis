package com.ekr.jularis.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ekr.jularis.R
import com.ekr.jularis.ui.admin.BottomMenuAdapter
import com.ekr.jularis.ui.admin.dashboard.DashboardFragment
import com.ekr.jularis.ui.admin.product.ProductFragment
import com.ekr.jularis.ui.cart.CartFragment
import com.ekr.jularis.ui.history.TransactionFragment
import com.ekr.jularis.ui.home.HomeFragment
import com.ekr.jularis.ui.setting.SettingFragment
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    var exit: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val sessionManager = SessionManager(this)
        if (sessionManager.prefIsLogin) {
            if (sessionManager.prefRole != "admin") {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
                finish()
            }
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fm_admin,
                DashboardFragment()
            ).commit()
        }
        animatedBottomBarAdmin.setItemSelected(R.id.navigation_home_admin)
        fragmentChanged()
    }

    private fun fragmentChanged() {

        animatedBottomBarAdmin.setOnItemSelectedListener { id ->
            var selectedFragment: Fragment? = null
            when (id) {
                R.id.navigation_home_admin -> selectedFragment = DashboardFragment()
                R.id.navigation_product -> selectedFragment = ProductFragment()
                R.id.navigation_trans_admin -> selectedFragment = TransactionFragment()
                R.id.navigation_setting_admin -> selectedFragment = SettingFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fm_admin,
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