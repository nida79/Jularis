package com.ekr.jularis.ui.paymentall

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.payment.DataGetPayment
import com.ekr.jularis.data.payment.DataPayment
import com.ekr.jularis.data.payment.DatapostPayment
import com.ekr.jularis.data.payment.DatapostPayment2
import com.ekr.jularis.data.response.ResponseGetDataPayment
import com.ekr.jularis.ui.MainActivity
import com.ekr.jularis.utils.LoadingDialog
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment_all.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import java.io.File

class PaymentActivityAll : AppCompatActivity(), PaymentAllContract.View {
    private lateinit var paymentAllPresenter: PaymentAllPresenter
    private lateinit var sessionManager: SessionManager
    private lateinit var paymentAdapter: PaymentAdapter
    private var checkedAmount = 0
    private var transactionAmount = 0
    private var ongkir = 0
    private var count = 0
    private lateinit var dialog: Dialog
    private var tipeBayar = "Bayar Ditempat"
    private var photo_id: String? = null
    private lateinit var data : List<DataGetPayment>
    private lateinit var dataKirim : List<DataPayment>
    private lateinit var datapostPayment: DatapostPayment2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_all)
        dialog = LoadingDialog.globalLoading(this)
        paymentAllPresenter = PaymentAllPresenter(this)
        sessionManager = SessionManager(this)
        edt_alamat_payment_all.setText(sessionManager.prefAlamat)

    }

    override fun onStart() {
        super.onStart()
        paymentAllPresenter.getDataPayment(sessionManager.prefToken)
    }
    override fun initListener() {

        paymentAdapter = PaymentAdapter(arrayListOf())
        rcv_payment.apply {
            layoutManager = LinearLayoutManager(this@PaymentActivityAll)
            adapter = paymentAdapter
            setHasFixedSize(true)
        }
        swp_paymnt_all.setOnRefreshListener {
            swp_paymnt_all.isRefreshing= false
            paymentAllPresenter.getDataPayment(sessionManager.prefToken)
        }
        radioGroup_all.setOnCheckedChangeListener { _, _ -> radioSelected() }
        uploadPhoto()
        btn_submit_payment_all.setOnClickListener {
            datapostPayment = DatapostPayment2(
                data,
                edt_alamat_payment_all.text.toString(),
                count,
                edt_catata_payment_all.text.toString(),
                checkedAmount,
                tipeBayar,
                ongkir,
                transactionAmount,
                photo_id
            )
            paymentAllPresenter.postDataPayment(sessionManager.prefToken,datapostPayment)
        }
    }

    override fun radioSelected() {
        if (bayar_transfer_all.isChecked) {
            btn_submit_payment_all.visibility = View.GONE
            tipeBayar = bayar_transfer_all.text.toString()
            lltrans_all.visibility = View.VISIBLE
            bayar_transfer_all.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorPrimary
                )
            )
            bayar_ditempat_all.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.abu_soft
                )
            )

        }
        if (bayar_ditempat_all.isChecked) {
            lltrans_all.visibility = View.GONE
            btn_submit_payment_all.visibility = View.VISIBLE
            tipeBayar = bayar_ditempat_all.text.toString()
            bayar_ditempat_all.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorPrimary
                )
            )
            bayar_transfer_all.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.abu_soft
                )
            )
        }
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                rcv_payment.visibility = View.INVISIBLE
                shimmer_container_paymentall.visibility = View.VISIBLE
                shimmer_container_paymentall.startShimmer()
                progress_bar_horizontal_payment_all.visibility = View.VISIBLE
            }
            false -> {
                rcv_payment .visibility = View.VISIBLE
                shimmer_container_paymentall.visibility = View.GONE
                shimmer_container_paymentall.stopShimmer()
                progress_bar_horizontal_payment_all.visibility = View.GONE
            }
        }
    }

    override fun loadingFoto(loadingFoto: Boolean) {
        when (loadingFoto) {
            true -> dialog.show()
            false -> dialog.dismiss()
        }
    }

    override fun onResultDataPayment(responseGetDataPayment: ResponseGetDataPayment,dataPayment: List<DataGetPayment>) {
        paymentAdapter.setData(responseGetDataPayment.data)
        data = dataPayment
        transactionAmount = responseGetDataPayment.transactionAmount
        ongkir = responseGetDataPayment.ongkir
        count = responseGetDataPayment.count
        checkedAmount = responseGetDataPayment.checkedAmount
        MoneyHelper.setRupiah(tv_total_price_payment_all, checkedAmount)
        MoneyHelper.setRupiah(tv_ongkir_payment_all, ongkir)
        MoneyHelper.setRupiah(tv_total_payment_all, transactionAmount)
        tv_total_qty_payment_all.text = count.toString()
    }

    override fun onResultUploadPhoto(photo_payment: String) {
        photo_id = photo_payment
        btn_submit_payment_all.visibility = View.VISIBLE
    }

    override fun resultPayment(sukses: Boolean) {
        if (sukses){
           startActivity(Intent(this,MainActivity::class.java))
            finishAffinity()
            finish()
        }
    }

    override fun showMessage(message: String) {
      Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }

    private fun uploadPhoto() {
        upload_foto_payment_all.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            ImagePicker.with(this@PaymentActivityAll)
                                .crop()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .start()
                        }

                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
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

    }

    private fun openSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Perizinan Diperlukan")
        builder.setMessage("Aktifkan Perizinan untuk Mengupload Gambar")
        builder.setPositiveButton("BUKA PENGATURAN") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
            openSetting()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                upload_foto_payment_all.setImageURI(fileUri)
                val file: File = ImagePicker.getFile(data)!!
                paymentAllPresenter.uploadFoto(sessionManager.prefToken, file)

            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
        bottomSheetDialog.show()
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.btn_sheet_cancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.btn_sheet_ok.setOnClickListener {
            bottomSheetDialog.dismiss()
            finish()
        }
    }
}