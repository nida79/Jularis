package com.ekr.jularis.ui.admin.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.response.ResponseSellingtoday
import com.ekr.jularis.data.response.ResponseTopselling
import com.ekr.jularis.databinding.FragmentDashboardBinding
import com.ekr.jularis.utils.Config
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import com.google.firebase.messaging.FirebaseMessaging
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
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)
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
            true -> {
                binding.rcvTopSelling.visibility = View.GONE
                binding.progressBarHorizontalDashboard.visibility = View.VISIBLE
                binding.shimmerTopselling.visibility = View.VISIBLE
                binding.shimmerTopselling.startShimmer()
            }
            false -> {
                binding.progressBarHorizontalDashboard.visibility = View.GONE
                binding.shimmerTopselling.stopShimmer()
                binding.shimmerTopselling.visibility = View.GONE
                binding.rcvTopSelling.visibility = View.VISIBLE
            }
        }
    }

    override fun initListener() {
        setupnotification()
        dashboard_NameProfile.text = sessionManager.prefFullname
        topsellingAdapter = TopsellingAdapter(arrayListOf())
        todaysellingAdapter = TodaysellingAdapter(arrayListOf())
        binding.rcvTopSelling.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            setHasFixedSize(true)
            adapter = topsellingAdapter
        }
        binding.rcvSellingToday.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            adapter = todaysellingAdapter
        }
        binding.swpDashboard.setOnRefreshListener {
            binding.swpDashboard.isRefreshing = false
            dashboardPresenter.getTotalAmount(sessionManager.prefToken)
            dashboardPresenter.getTopSelling(sessionManager.prefToken)
            dashboardPresenter.getSellingToday(sessionManager.prefToken)
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun resultTotalAmount(uang: Int) {
        if (uang!=null){
            MoneyHelper.setRupiah(dashboard_BalanceProfile, uang)
        }else{
            dashboard_BalanceProfile.text = "Loading.."
        }

    }

    override fun resultTopSelling(responseTopselling: ResponseTopselling) {
        responseTopselling.data?.let { topsellingAdapter.setData(it) }
    }

    override fun resultSellingToday(responseSellingtoday: ResponseSellingtoday) {
        MoneyHelper.setRupiah(binding.amountToday, responseSellingtoday.transactionAmountToday)
        todaysellingAdapter.setData(responseSellingtoday.productSelling)
    }

    private fun setupnotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Config.CHANNEL_ID, Config.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT

            )
            channel.description = Config.CHANNEL_DESC

            val manager: NotificationManager =
                ContextCompat.getSystemService(requireContext(), NotificationManager::class.java)!!
            manager.createNotificationChannel(channel)
        }
    }
}