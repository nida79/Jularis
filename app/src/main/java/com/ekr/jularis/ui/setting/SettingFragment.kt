package com.ekr.jularis.ui.setting

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekr.jularis.data.product.OngkirData
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.databinding.FragmentSettingBinding
import com.ekr.jularis.ui.login.LoginActivity
import com.ekr.jularis.ui.profile.ProfileActivity
import com.ekr.jularis.utils.*
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.bottom_sheet_logout.*
import kotlinx.android.synthetic.main.change_password.*
import kotlinx.android.synthetic.main.change_password.close_cp
import kotlinx.android.synthetic.main.dialog_set_ongkir.*
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : Fragment(), SettingContract.View {
    private lateinit var settingPresenter: SettingPresenter
    private lateinit var sessionManager: SessionManager
    private lateinit var settingBinding: FragmentSettingBinding
    private lateinit var dialog: Dialog
    private lateinit var dialogOngkir: Dialog
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingPresenter = SettingPresenter(this)
        sessionManager = SessionManager(requireActivity())
        if (sessionManager.prefIsLogin) {
            tv_name_setting.text = sessionManager.prefFullname
            btn_logout.visibility = View.VISIBLE
            setting_wadah_profile.visibility = View.VISIBLE
            setting_wadah_cp.visibility = View.VISIBLE
            setting_btn_login.visibility = View.GONE
            if (sessionManager.prefRole == "admin") {
                settingBinding.settingWadahAktifitas.visibility = View.VISIBLE
                settingBinding.rlvSettingOngkir.visibility = View.VISIBLE
            }
            GlideHelper.setImage(requireContext(), sessionManager.prefFoto, img_profile_setting)
        }
        if (!sessionManager.prefIsLogin) {
            btn_logout.visibility = View.GONE
            setting_wadah_profile.visibility = View.GONE
            setting_wadah_cp.visibility = View.GONE
            setting_btn_login.visibility = View.VISIBLE
        }

        dialogOngkir = DialogHelper.ongkirDialog(requireActivity())
        dialog = DialogHelper.changePasswordDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingBinding = FragmentSettingBinding.inflate(layoutInflater)
        return settingBinding.root
    }

    override fun initListener() {

        settingBinding.settingWadahCp.setOnClickListener {
            dialog.show()
            dialog.close_cp.setOnClickListener {
                dialog.tie_cp_new.setText("")
                dialog.tie_cp_old.setText("")
                dialog.dismiss()
            }
            dialog.btn_cp_submit.setOnClickListener {
                when {
                    dialog.tie_cp_new.text.toString().isEmpty() -> {
                        dialog.tie_cp_new.error = "Kolom Tidak Boleh Kosong"
                    }
                    dialog.tie_cp_new.text.toString().isEmpty() -> {
                        dialog.tie_cp_old.error = "Kolom Tidak Boleh Kosong"
                    }
                    else -> {
                        dialog.btn_cp_submit.visibility = View.GONE
                        dialog.spin_kit_cp.visibility = View.VISIBLE
                        settingPresenter.changePassword(
                            sessionManager.prefToken,
                            dialog.tie_cp_old.text.toString(),
                            dialog.tie_cp_new.text.toString()
                        )
                    }
                }

            }
        }

        settingBinding.rlvSettingOngkir.setOnClickListener {
            dialogOngkir.show()
            dialogOngkir.btn_ongkir_submit.setOnClickListener {
                dialogOngkir.btn_ongkir_submit.visibility = View.GONE
                dialogOngkir.spin_kit_ongkir.visibility = View.VISIBLE
                if (dialogOngkir.edt_ongkir_first.text.toString()
                        .isEmpty() || dialogOngkir.edt_ongkir_second.text.toString()
                        .isEmpty() || dialogOngkir.edt_ongkir_third.text.toString()
                        .isEmpty() || dialogOngkir.edt_ongkir_four.text.toString().isEmpty()
                ) {
                    val ongkirData = OngkirData(
                        null, null, null, null
                    )
                    settingPresenter.setOngkir(sessionManager.prefToken, ongkirData)
                }else{
                    val ongkirData = OngkirData(
                        dialogOngkir.edt_ongkir_first.text.toString().toInt(),
                        dialogOngkir.edt_ongkir_second.text.toString().toInt(),
                        dialogOngkir.edt_ongkir_third.text.toString().toInt(),
                        dialogOngkir.edt_ongkir_four.text.toString().toInt()
                    )
                    settingPresenter.setOngkir(sessionManager.prefToken, ongkirData)
                }

            }
            dialogOngkir.close_ongkir.setOnClickListener { dialogOngkir.dismiss() }
        }

        setting_wadah_profile.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            requireActivity().startActivity(intent)
        }

        setting_btn_login.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        btn_logout.setOnClickListener {
            val bottomSheetDialog = DialogHelper.bottomSheetDialogLogout(requireActivity())
            bottomSheetDialog.show()
            bottomSheetDialog.btn_sheet_iya.setOnClickListener {
                if (sessionManager.prefRole != "user") {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL)
                }
                NotificationHelper.logoutFCM()
                settingPresenter.doLogout(sessionManager.prefToken)
                bottomSheetDialog.dismiss()
            }
        }
    }

    override fun loadingLogout(loading: Boolean) {
        when (loading) {
            false -> {
                settingBinding.spinKitLogout.visibility = View.GONE
                btn_logout.visibility = View.VISIBLE
            }
            true -> {
                settingBinding.spinKitLogout.visibility = View.VISIBLE
                btn_logout.visibility = View.GONE
            }
        }
    }


    override fun resultChangePassword(boolean: Boolean) {
        if (boolean) {
            dialog.spin_kit_cp.visibility = View.GONE
            dialog.btn_cp_submit.visibility = View.VISIBLE
            dialog.tie_cp_new.setText("")
            dialog.tie_cp_old.setText("")
            dialog.dismiss()
        } else {
            dialog.spin_kit_cp.visibility = View.GONE
            dialog.btn_cp_submit.visibility = View.VISIBLE
        }
    }

    override fun resultOngkir(hasil: Boolean) {
        if (hasil) {
            dialogOngkir.spin_kit_ongkir.visibility = View.GONE
            dialogOngkir.btn_ongkir_submit.visibility = View.VISIBLE
            dialogOngkir.edt_ongkir_first.setText("")
            dialogOngkir.edt_ongkir_second.setText("")
            dialogOngkir.edt_ongkir_third.setText("")
            dialogOngkir.edt_ongkir_four.setText("")
            dialogOngkir.dismiss()
        }else {
            dialogOngkir.spin_kit_ongkir.visibility = View.GONE
            dialogOngkir.btn_ongkir_submit.visibility = View.VISIBLE
        }
    }


    override fun resultLogout(status: Boolean) {
        if (status) {
            btn_logout.visibility = View.GONE
            setting_wadah_profile.visibility = View.GONE
            setting_wadah_cp.visibility = View.GONE
            setting_btn_login.visibility = View.VISIBLE
            sessionManager.logOut()
            if (sessionManager.prefRole != "user") {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL)
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.putExtra("admin", "admin")
                startActivity(intent)
            } else {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.putExtra("logout", "logout")
                startActivity(intent)
            }

        }
    }

    override fun showMessage(message: String) {
        Toasty.info(requireContext(), message, Toasty.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }
}