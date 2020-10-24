package com.ekr.jularis.ui.history.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.histori.HistoriData
import com.ekr.jularis.data.histori.HistoriIUpdate
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_update.*


class TransactionActivityDetail : AppCompatActivity(), TransactionDetailContract.View {
    private lateinit var update: TransactionPresenter
    private lateinit var sessionManager: SessionManager
    private lateinit var transactionDetailAdapter: TransactionDetailAdapter
    private lateinit var historiIUpdate: HistoriIUpdate
    private lateinit var historiData: HistoriData
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
        historiData = intent.getParcelableExtra("data")!!
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
}