package com.ekr.jularis.ui.admin.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekr.jularis.R
import com.ekr.jularis.databinding.FragmentDashboardBinding
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.profile_dashboard.*

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager= SessionManager(requireContext())
        GlideHelper.setImage(requireContext(),sessionManager.prefFoto,dashboard_ImgProfile)
        dashboard_NameProfile.text = sessionManager.prefFullname
        dashboard_BalanceProfile.text = "Rp.500.000.000"
    }
}