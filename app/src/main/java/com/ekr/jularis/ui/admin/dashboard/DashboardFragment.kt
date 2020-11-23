package com.ekr.jularis.ui.admin.dashboard

import android.Manifest
import android.app.Dialog
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.dashboard.PostDownload
import com.ekr.jularis.data.response.ResponseGetReport
import com.ekr.jularis.data.response.ResponseSellingtoday
import com.ekr.jularis.data.response.ResponseTopselling
import com.ekr.jularis.databinding.FragmentDashboardBinding
import com.ekr.jularis.utils.*
import com.github.dewinjm.monthyearpicker.MonthFormat
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.profile_dashboard.*
import okhttp3.ResponseBody
import java.util.*


class DashboardFragment : Fragment(R.layout.fragment_dashboard), DashboardContract.View {
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var dashboardPresenter: DashboardPresenter
    private lateinit var topsellingAdapter: TopsellingAdapter
    private lateinit var todaysellingAdapter: TodaysellingAdapter
    private var tahun = ""
    private var bulan = ""
    private val REQUEST_CODE = 17
    private lateinit var dialog: Dialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = DialogHelper.globalLoading(requireActivity())
        sessionManager = SessionManager(requireContext())
        dashboardPresenter = DashboardPresenter(this)
        GlideHelper.setImage(requireContext(), sessionManager.prefFoto, dashboard_ImgProfile)
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)

    }

    override fun onStart() {
        super.onStart()

        dashboardPresenter.doCheckUpdate(requireActivity(),REQUEST_CODE)
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

    override fun resultGetUrl(responseGetReport: ResponseGetReport) {
        if (responseGetReport.status) {
          dashboardPresenter.downloadReport(requireActivity(),responseGetReport.url)

        } else {
            showMessage("Download Tidak Berhasil")
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
        btn_dashboard_laporan.setOnClickListener {
            requestPermision()
        }
    }

    override fun onResume() {
        super.onResume()
        dashboardPresenter.doCheckUpdate(requireActivity(),REQUEST_CODE)
    }

    private fun requestPermision() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val calendar: Calendar = Calendar.getInstance()
                        val yearSelected = calendar.get(Calendar.YEAR)
                        val monthSelected = calendar.get(Calendar.MONTH)
                        val localeID = Locale("in", "ID")
                        val dialogFragment = MonthYearPickerDialogFragment
                            .getInstance(monthSelected, yearSelected, "Pilih Bulan", localeID)
                        dialogFragment.show(childFragmentManager, null)
                        dialogFragment.setOnDateSetListener { year, monthOfYear ->
                            when (monthOfYear) {
                                0 -> {
                                    bulan = "January"
                                }
                                1 -> {
                                    bulan = "February"
                                }
                                2 -> {
                                    bulan = "March"
                                }
                                3 -> {
                                    bulan = "April"
                                }
                                4 -> {
                                    bulan = "May"
                                }
                                5 -> {
                                    bulan = "June"
                                }
                                6 -> {
                                    bulan = "July"
                                }
                                7 -> {
                                    bulan = "August"
                                }
                                8 -> {
                                    bulan = "September"
                                }
                                9 -> {
                                    bulan = "October"
                                }
                                10 -> {
                                    bulan = "November"
                                }
                                11 -> {
                                    bulan = "December"
                                }
                            }
                            tahun = year.toString()
                            val postDownload = PostDownload(bulan, tahun)
                            dashboardPresenter.dogetReport(sessionManager.prefToken, postDownload)

                        }
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(
                            requireContext(),
                            "Mohon Aktifkan Perizinan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toasty.error(
                    requireContext(), "Error occurred! ", Toasty.LENGTH_LONG
                ).show()
            }
            .onSameThread()
            .check()

    }


    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDownloadProgress(loading: Boolean) {
        when (loading) {
            true -> dialog.show()
            false -> dialog.dismiss()
        }
    }



    override fun resultTotalAmount(uang: Int) {
        if (uang != 0 ||uang  != null) {
            MoneyHelper.setRupiah(dashboard_BalanceProfile, uang)
        } else {
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            Toasty.info(requireContext(), "Update Dilakukan", Toasty.LENGTH_LONG).show()
            if (resultCode != AppCompatActivity.RESULT_OK) {
                Toasty.error(
                    requireContext(),
                    "Terjadi Kesalahan, Gagal Update Aplikasi",
                    Toasty.LENGTH_LONG
                ).show()
                Log.e("Error Update", "Perbarui Gagal $resultCode")

            }
        }
        if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_CANCELED) {
            Toasty.warning(requireContext(), "Batal Update", Toasty.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }
}