package com.ekr.jularis.ui.history.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ekr.jularis.R
import com.ekr.jularis.data.histori.HistoriData
import com.ekr.jularis.data.histori.HistoriIUpdate
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*

class TransactionActivityDetail : AppCompatActivity(), TransactionDetailContract.View {
    private lateinit var update: TransactionPresenter
    private var historiData: HistoriData? = null
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
        sessionManager = SessionManager(this)
        update = TransactionPresenter(this)

    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        historiData = intent.extras?.getParcelable("data")
        when {
            sessionManager.prefRole != "user" -> {
                btn_konfirmasi.visibility = View.VISIBLE
                btn_konfirmasi.setOnClickListener {
                    val dialog = DialogHelper.bottomSheetDialogKonfirm(this)
                    dialog.show()
                    dialog.text_keterangan.text = "Update Status Pesanan ?"
                    dialog.btn_sheet_cancel.text = "Konfirmasi"
                    dialog.btn_sheet_cancel.setOnClickListener {
                        val kirim = HistoriIUpdate("Pesanan Dikirim")
                        update.doUpdate(sessionManager.prefToken,
                            historiData!!.transactionProductId,kirim)
                        dialog.dismiss()
                    }
                    dialog.btn_sheet_ok.text = "Batal"
                    dialog.btn_sheet_ok.setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
            else -> {
                btn_konfirmasi.visibility = View.GONE
            }
        }
        tvStatus_detail_pesanan.text = historiData!!.transactionState
        if (historiData!!.transactionState == "Selesai") {
            btn_konfirmasi.visibility = View.GONE
            tvStatus_detail_pesanan.text = "Barang Telah Diterima"
        }
        val foto = let { historiData?.transaction_photo_transfer }
        if (foto!=null){
            GlideHelper.setImage(applicationContext,foto,img_bukti_detail_pesanan)
            img_bukti_detail_pesanan.visibility = View.VISIBLE
            tv_bukti_Detail_pesanan.visibility = View.VISIBLE
        }

        GlideHelper.setImage(this, historiData!!.pictureProductTransaction, img_detail_pesanan)
        tv_titile_detail_pesanan.text = historiData!!.productName
        MoneyHelper.setRupiah(tv_harga_detail_pesanan, historiData!!.historiProduct.price)
        tvAlamat_detail_pesanan.text = historiData!!.address
        if (historiData!!.note.equals("")|| historiData!!.note == "null") {
            tvCatatan_detail_pesanan.text = "Tidak Ada Catatan"
        } else {
            tvCatatan_detail_pesanan.text = historiData!!.note
        }
        tv_qty_detail_pesanan.text = historiData!!.quantity.toString()
        MoneyHelper.setRupiah(tv_total_price_detail_pesanan, historiData!!.productAmount)
        MoneyHelper.setRupiah(tv_ongkir_detail_pesanan, historiData!!.historiProduct.ongkir)
        MoneyHelper.setRupiah(tv_total_detail_pesanan, historiData!!.productAmount)


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