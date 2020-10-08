package com.ekr.jularis.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ekr.jularis.R
import com.ekr.jularis.ui.cart.CartFragment
import com.ekr.jularis.ui.history.TransactionFragment
import com.ekr.jularis.ui.home.HomeFragment
import com.ekr.jularis.ui.setting.SettingFragment
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var exit: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sessionManager = SessionManager(this)
        if (sessionManager.prefIsLogin){
            if (sessionManager.prefRole!="user"){
                startActivity(Intent(this,MainActivity2::class.java))
                finishAffinity()
                finish()
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                HomeFragment()
            ).commit()
        }
        animatedBottomBar.setItemSelected(R.id.navigation_home)
        fragmentChanged()
    }

    private fun fragmentChanged() {

        animatedBottomBar.setOnItemSelectedListener { id ->
            var selectedFragment: Fragment? = null
            when (id) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_trans -> selectedFragment = TransactionFragment()
                R.id.navigation_cart -> selectedFragment = CartFragment()
                R.id.navigation_setting -> selectedFragment = SettingFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
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