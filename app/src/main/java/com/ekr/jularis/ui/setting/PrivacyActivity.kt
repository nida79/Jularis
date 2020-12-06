package com.ekr.jularis.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ekr.jularis.R
import kotlinx.android.synthetic.main.activity_privacy.*

class PrivacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        privacy_webView.loadUrl("file:///android_asset/ppolicy.html")
    }

    override fun onBackPressed() {
        finish()
    }
}