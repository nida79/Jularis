package com.ekr.jularis.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ekr.jularis.R
import com.ekr.jularis.ui.login.LoginActivity
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.SessionManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : Fragment(), SettingContract.View {
    private lateinit var settingPresenter: SettingPresenter
    private lateinit var sessionManager: SessionManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingPresenter = SettingPresenter(this)
        sessionManager = SessionManager(requireActivity())
        tv_name_setting.text = sessionManager.prefFullname
        if (!sessionManager.prefIsLogin){
            btn_logout.visibility = View.GONE
            setting_wadah_profile.visibility = View.GONE
            setting_wadah_cp.visibility = View.GONE
            setting_btn_login.visibility = View.VISIBLE
        }else{
            GlideHelper.setImage(requireContext(),sessionManager.prefFoto,img_profile_setting)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun initListener() {
        setting_btn_login.setOnClickListener {
            startActivity(Intent(requireActivity(),LoginActivity::class.java))
        }
        btn_logout.setOnClickListener {
            settingPresenter.doLogout(sessionManager.prefToken)
        }
    }

    override fun loadingLogout(loading: Boolean) {
        when (loading) {
            false -> {
                spin_kit_logout.visibility = View.GONE
                btn_logout.visibility = View.VISIBLE
            }
            true -> {
                spin_kit_logout.visibility = View.VISIBLE
                btn_logout.visibility = View.GONE
            }
        }
    }

    override fun resultLogout(status: Boolean) {
        if (status) {
            val intent = Intent(requireActivity(),LoginActivity::class.java)
            intent.putExtra("logout","logout")
            startActivity(intent)
            sessionManager.logOut()
            requireActivity().finishAffinity()
            requireActivity().finish()
        }
    }

    override fun showMessage(message: String) {
        Toasty.info(requireContext(), message, Toasty.LENGTH_SHORT).show()
    }

}