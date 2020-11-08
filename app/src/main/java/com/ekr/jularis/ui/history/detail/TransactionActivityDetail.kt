package com.ekr.jularis.ui.history.detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.dashboard.PostDownload
import com.ekr.jularis.data.histori.HistoriData
import com.ekr.jularis.data.histori.HistoriIUpdate
import com.ekr.jularis.data.histori.ReportDaily
import com.ekr.jularis.data.response.ResponseGetReport
import com.ekr.jularis.utils.*
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_update.*
import java.util.*


class TransactionActivityDetail : AppCompatActivity(), TransactionDetailContract.View {
    private lateinit var update: TransactionPresenter
    private lateinit var sessionManager: SessionManager
    private lateinit var transactionDetailAdapter: TransactionDetailAdapter
    private lateinit var historiIUpdate: HistoriIUpdate
    private lateinit var historiData: HistoriData
    private lateinit var reportDaily: ReportDaily
    private lateinit var dialog: Dialog
    private lateinit var global: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
        global = DialogHelper.globalLoading(this)
        sessionManager = SessionManager(this)
        update = TransactionPresenter(this)
        dialog = DialogHelper.bottomSheetDialogUpdate(this)


    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        setupnotification()
        historiData = intent.getParcelableExtra("data")!!
        reportDaily = ReportDaily(
            historiData.transaction_date_format,
            historiData.transaction_date_format
        )
        transactionDetailAdapter = TransactionDetailAdapter(arrayListOf())
        transactionDetailAdapter.setData(historiData.product_list)
        rcv_transactionDetail.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(
                    applicationContext, LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = transactionDetailAdapter

        }
        when {
            historiData.transactionState == "Selesai" -> {
                btn_konfirmasi.visibility = View.GONE
            }
            historiData.transactionState == "Dikirim" -> {
                btn_konfirmasi.visibility = View.VISIBLE
                btn_konfirmasi.setOnClickListener {
                    dialog.text_keterangan_update.text =
                        "Konfirmasi Update! Update Status Pengiriman Menjadi Selesai ?"
                    dialog.show()
                    dialog.btn_sheet_update_ok.setOnClickListener {
                        historiIUpdate = HistoriIUpdate("Selesai")
                        update.doUpdate(
                            sessionManager.prefToken,
                            historiData.transactionId,
                            historiIUpdate
                        )
                        dialog.dismiss()
                    }
                    dialog.btn_sheet_update_cancel.setOnClickListener { dialog.dismiss() }
                }
            }
            sessionManager.prefRole == "user" -> {
                btn_konfirmasi.visibility = View.GONE
            }
            sessionManager.prefRole != "user" && historiData.transactionState == "Menunggu Konfirmasi" -> {
                btn_konfirmasi.setOnClickListener {
                    dialog.text_keterangan_update.text =
                        "Konfirmasi Update! Update Status Pengiriman Menjadi Dikirim ?"
                    dialog.show()
                    dialog.btn_sheet_update_ok.setOnClickListener {
                        historiIUpdate = HistoriIUpdate("Dikirim")
                        update.doUpdate(
                            sessionManager.prefToken,
                            historiData.transactionId,
                            historiIUpdate
                        )
                        dialog.dismiss()
                    }
                    dialog.btn_sheet_update_cancel.setOnClickListener { dialog.dismiss() }

                }
            }

        }
        setupView(historiData)
        download_Daily.setOnClickListener {
        requestPermision()
        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setupView(historiData: HistoriData) {
        tv_metodePayment_detail_pesanan.text = historiData.paymentMethod
        if (historiData.paymentMethod == "Transfer Bank") {
            tv_bukti_Detail_pesanan.visibility = View.VISIBLE
            historiData.transactionPhotoTransfer?.let {
                GlideHelper.setImage(
                    applicationContext,
                    it, img_bukti_detail_pesanan
                )
            }
            img_bukti_detail_pesanan.visibility = View.VISIBLE
        }
        tvStatus_detail_pesanan.text = historiData.transactionState
        tvAlamat_detail_pesanan.text = historiData.address
        tvNohp_detail_pesanan.text = historiData.userPhone
        tvCatatan_detail_pesanan.text = historiData.note
        tv_qty_detail_pesanan.text = historiData.quantityTotal.toString()
        MoneyHelper.setRupiah(tv_total_price_detail_pesanan, historiData.productAmount)
        MoneyHelper.setRupiah(tv_ongkir_detail_pesanan, historiData.servicePrice)
        MoneyHelper.setRupiah(tv_total_detail_pesanan, historiData.transactionAmount)
        btn_maps.setOnClickListener {
            val googlemap = "com.google.android.apps.maps"
            val gmmIntentUri = Uri.parse("google.navigation:q=${historiData.address}") as Uri
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage(googlemap)
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Google Maps Belum Terinstal, Install Terlebih dahulu.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    override fun onResultUpdate(berhasil: Boolean) {
        if (berhasil) {
            global.dismiss()
            finish()
        }
    }

    private fun requestPermision() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        update.doGetUrl(sessionManager.prefToken, reportDaily)
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(
                            applicationContext,
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
                    applicationContext, "Error occurred! ", Toasty.LENGTH_LONG
                ).show()
            }
            .onSameThread()
            .check()

    }

    override fun onResultDwonload(responseGetReport: ResponseGetReport) {
        if (responseGetReport.status) {
            update.downloadReport(this,responseGetReport.url)

        } else {
            showMessage("Download Tidak Berhasil")
        }
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                global.show()
            }
            false -> {
                global.dismiss()
            }
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun setupnotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Config.CHANNEL_ID, Config.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT

            )
            channel.description = Config.CHANNEL_DESC

            val manager: NotificationManager =
                ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)!!
            manager.createNotificationChannel(channel)
        }
    }
}