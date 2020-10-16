package com.ekr.jularis.ui.admin.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.response.ResponseSellingtoday
import com.ekr.jularis.data.response.ResponseTopselling
import com.ekr.jularis.databinding.FragmentDashboardBinding
import com.ekr.jularis.databinding.FragmentSelesaiBinding
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.profile_dashboard.*

class DashboardFragment : Fragment(R.layout.fragment_dashboard), DashboardContract.View {
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var dashboardPresenter: DashboardPresenter
    private lateinit var topsellingAdapter: TopsellingAdapter
    private lateinit var todaysellingAdapter: TodaysellingAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        dashboardPresenter = DashboardPresenter(this)
        GlideHelper.setImage(requireContext(), sessionManager.prefFoto, dashboard_ImgProfile)

    }

    override fun onStart() {
        super.onStart()
        dashboardPresenter.getTotalAmount(sessionManager.prefToken)
        dashboardPresenter.getTopSelling(sessionManager.prefToken)
        dashboardPresenter.getSellingToday(sessionManager.prefToken)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> binding.progressBarHorizontalDashboard.visibility = View.VISIBLE
            false -> binding.progressBarHorizontalDashboard.visibility = View.GONE
        }
    }

    override fun initListener() {
        dashboard_NameProfile.text = sessionManager.prefFullname
        topsellingAdapter = TopsellingAdapter(arrayListOf())
        todaysellingAdapter = TodaysellingAdapter(arrayListOf())
        binding.rcvTopSelling.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,
                    false)
            setHasFixedSize(true)
            adapter = topsellingAdapter
        }
        binding.rcvSellingToday.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = todaysellingAdapter
        }
        binding.swpDashboard.setOnRefreshListener {
            binding.swpDashboard.isRefreshing = false
            dashboardPresenter.getTopSelling(sessionManager.prefToken)
            dashboardPresenter.getSellingToday(sessionManager.prefToken)
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun resultTotalAmount(uang: Int) {
        MoneyHelper.setRupiah(dashboard_BalanceProfile, uang)
    }

    override fun resultTopSelling(responseTopselling: ResponseTopselling) {
        responseTopselling.data?.let { topsellingAdapter.setData(it) }
    }

    override fun resultSellingToday(responseSellingtoday: ResponseSellingtoday) {
        MoneyHelper.setRupiah(binding.amountToday, responseSellingtoday.transactionAmountToday)
        todaysellingAdapter.setData(responseSellingtoday.productSelling)
    }
}