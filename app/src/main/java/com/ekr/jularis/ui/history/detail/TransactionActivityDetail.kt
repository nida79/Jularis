package com.ekr.jularis.ui.history.detail

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.histori.HistoriIUpdate
import com.ekr.jularis.data.histori.HistoriData
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.bottom_sheet_update.*

class TransactionActivityDetail : AppCompatActivity(), TransactionDetailContract.View {
    private lateinit var update: TransactionPresenter
    private lateinit var sessionManager: SessionManager
    private lateinit var transactionDetailAdapter: TransactionDetailAdapter
    private lateinit var historiIUpdate: HistoriIUpdate
    private lateinit var historiData: HistoriData
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
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
            sessionManager.prefRole != "admin" -> {
                btn_konfirmasi.visibility = View.GONE
            }
            sessionManager.prefRole == "user" && historiData.transactionState == "Dikirim" -> {
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
            sessionManager.prefRole != "user" && historiData.transactionState == "Dikirim" -> {
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
        }
        setupView(historiData)

    }

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
    }

    override fun onResultUpdate(berhasil: Boolean) {
        if (berhasil) {
            finish()
        }
    }

    override fun onLoading(loading: Boolean) {
        val dialog = DialogHelper.globalLoading(this)
        when (loading) {
            true -> {
                dialog.show()
            }
            false -> {
                dialog.dismiss()
            }
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}