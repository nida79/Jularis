package com.ekr.jularis.ui.history.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.histori.HistoriData
import com.ekr.jularis.data.histori.HistoriIUpdate
import com.ekr.jularis.data.response.HistoriNewData
import com.ekr.jularis.data.response.HistoriNewProduct
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*

class TransactionActivityDetail : AppCompatActivity(), TransactionDetailContract.View {
    private lateinit var update: TransactionPresenter
    private lateinit var sessionManager: SessionManager
    private lateinit var transactionDetailAdapter: TransactionDetailAdapter
    private lateinit var historiIUpdate: HistoriIUpdate
    private lateinit var historiNewData: HistoriNewData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
        sessionManager = SessionManager(this)
        update = TransactionPresenter(this)

    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        historiNewData = intent.getParcelableExtra("data")!!
        transactionDetailAdapter = TransactionDetailAdapter(arrayListOf())
        transactionDetailAdapter.setData(historiNewData.product_list)
        rcv_transactionDetail.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,
                    false)
            adapter = transactionDetailAdapter

        }
        when{
            historiNewData.transactionState =="Selesai"->{
                btn_konfirmasi.visibility = View.GONE
            }
            sessionManager.prefRole!="admin"->{
                btn_konfirmasi.visibility = View.GONE
            }
            sessionManager.prefRole =="user" && historiNewData.transactionState =="Dikirim"->{
                btn_konfirmasi.visibility = View.VISIBLE
                btn_konfirmasi.setOnClickListener {
                    historiIUpdate = HistoriIUpdate("Selesai")
                    update.doUpdate(sessionManager.prefToken,historiNewData.transactionId,historiIUpdate)
                }
            }
        }
        setupView(historiNewData)

    }

    private fun setupView(historiNewData: HistoriNewData) {
        tv_metodePayment_detail_pesanan.text = historiNewData.paymentMethod
        if (historiNewData.paymentMethod == "Transfer Bank"){
            tv_bukti_Detail_pesanan.visibility = View.VISIBLE
            historiNewData.transactionPhotoTransfer?.let {
                GlideHelper.setImage(applicationContext,
                    it,img_bukti_detail_pesanan)
            }
            img_bukti_detail_pesanan.visibility=View.VISIBLE
        }
        tvStatus_detail_pesanan.text = historiNewData.transactionState
        tvAlamat_detail_pesanan.text = historiNewData.address
        tvNohp_detail_pesanan.text=historiNewData.userPhone
        tvCatatan_detail_pesanan.text = historiNewData.note
        tv_qty_detail_pesanan.text = historiNewData.quantityTotal.toString()
        MoneyHelper.setRupiah(tv_total_price_detail_pesanan,historiNewData.productAmount)
        MoneyHelper.setRupiah(tv_ongkir_detail_pesanan,historiNewData.servicePrice)
        MoneyHelper.setRupiah(tv_total_detail_pesanan,historiNewData.transactionAmount)
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