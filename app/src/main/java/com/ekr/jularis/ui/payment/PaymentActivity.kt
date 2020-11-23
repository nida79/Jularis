package com.ekr.jularis.ui.payment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ekr.jularis.R
import com.ekr.jularis.data.payment.DataAlamat
import com.ekr.jularis.data.payment.DataPayment
import com.ekr.jularis.data.payment.DatapostPayment
import com.ekr.jularis.data.response.ResponseGetDataPayment
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.DialogHelper
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
import kotlinx.android.synthetic.main.dialog_changeaddress.*
import java.io.File

class PaymentActivity : AppCompatActivity(), PaymentContract.View {
    private var checkedAmount = 0
    private var transactionAmount = 0
    private var ongkir = 0
    private var count = 0
    private var gantiAlamat = ""
    private lateinit var dataAlamat: DataAlamat
    private lateinit var dialog: Dialog
    private var tipeBayar = "Bayar Ditempat"
    private lateinit var sessionManager: SessionManager
    private lateinit var paymentPresenter: PaymentPresenter
    private lateinit var product_id: String
    private var photo_id: String? = null
    private lateinit var dataPayment: DataPayment
    private lateinit var datapostPayment: DatapostPayment
    private lateinit var dialogChange: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        dialog = DialogHelper.globalLoading(this)
        dialogChange = DialogHelper.changeAddressDialog(this)
        paymentPresenter = PaymentPresenter(this)
        sessionManager = SessionManager(this)
        product_id = intent.getStringExtra("pd_id").toString()
        edt_alamat_payment.setText(sessionManager.prefAlamat)
        edt_no_hp_payment.setText(sessionManager.prefNohp)
        paymentPresenter.getDataPayment(sessionManager.prefToken, product_id)
        edt_alamat_payment.showSoftInputOnFocus = false
    }

    override fun initListener() {
        btn_submit_payment.setOnClickListener {
            when{
                edt_alamat_payment.text.toString()==""->{
                    edt_alamat_payment.error = "Alamat Tidak Boleh Kosong"
                    edt_alamat_payment.requestFocus()
                }
                edt_no_hp_payment.text.toString()==""->{
                    edt_no_hp_payment.error = "No Hp Tidak Boleh Kosong"
                    edt_no_hp_payment.requestFocus()
                }
            }
            datapostPayment = DatapostPayment(
                listOf(dataPayment),
                edt_alamat_payment.text.toString(),
                count,
                edt_catata_payment.text.toString(),
                checkedAmount,
                tipeBayar,
                ongkir,
                transactionAmount,
                photo_id,
                edt_no_hp_payment.text.toString()
            )
            paymentPresenter.postDataPayment(sessionManager.prefToken, datapostPayment)
        }
        radioGroup.setOnCheckedChangeListener { _, _ ->
            radioSelected()
        }
        edt_alamat_payment.setOnClickListener {
            dialogChange.show()
            dialogChange.cancel_changealamat.setOnClickListener {
                dialogChange.dismiss()
            }
            dialogChange.btn_changealamat.setOnClickListener {
                gantiAlamat = dialogChange.change_alamat.text.toString()
                dataAlamat = DataAlamat(gantiAlamat)
                if (gantiAlamat.isEmpty()) {
                    dialogChange.change_alamat.requestFocus()
                    dialogChange.change_alamat.error = "Kolom Tidak Boleh Kosong"

                } else {
                    paymentPresenter.changeAlamat(
                        sessionManager.prefToken,
                        product_id,
                        dataAlamat
                    )
                }
            }
        }
        uploadPhoto()

    }

    override fun radioSelected() {
        if (bayar_transfer.isChecked) {
            btn_submit_payment.visibility = View.GONE
            tipeBayar = bayar_transfer.text.toString()
            lltrans.visibility = View.VISIBLE
            bayar_transfer.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorPrimary
                )
            )
            bayar_ditempat.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.abu_soft
                )
            )

        }
        if (bayar_ditempat.isChecked) {
            lltrans.visibility = View.GONE
            btn_submit_payment.visibility = View.VISIBLE
            tipeBayar = bayar_ditempat.text.toString()
            bayar_ditempat.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorPrimary
                )
            )
            bayar_transfer.setTextColor(
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
                dialog.show()
            }
            false -> {
                dialog.dismiss()
            }
        }
    }

    override fun loadingFoto(loadingFoto: Boolean) {
        when (loadingFoto) {
            true -> {
                dialog.show()
                progress_bar_horizontal_payment.visibility = View.VISIBLE
            }
            false -> {
                dialog.dismiss()
                progress_bar_horizontal_payment.visibility = View.GONE
            }
        }
    }

    override fun loadingChangeAddress(loadingCA: Boolean) {
        when(loadingCA){
            true -> {
                dialogChange.spin_kit_ca.visibility = View.VISIBLE
                dialogChange.btn_changealamat.visibility = View.GONE
            }
            false -> {
                dialogChange.spin_kit_ca.visibility = View.GONE
                dialogChange.btn_changealamat.visibility = View.VISIBLE
            }
        }
    }

    override fun resultChangeAddress(hasil: Boolean) {
        if (hasil) {
            dialogChange.change_alamat.setText(gantiAlamat)
            edt_alamat_payment.setText(gantiAlamat)
            dialogChange.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResultDataPayment(responseGetDataPayment: ResponseGetDataPayment) {
        dataPayment = DataPayment(product_id, responseGetDataPayment.data[0].quantity)
        transactionAmount = responseGetDataPayment.transactionAmount
        ongkir = responseGetDataPayment.ongkir
        count = responseGetDataPayment.count
        checkedAmount = responseGetDataPayment.checkedAmount
        GlideHelper.setImage(
            applicationContext,
            responseGetDataPayment.data[0].picturePayment.picture, img_item_payment
        )
        tv_title_payment.text = responseGetDataPayment.data[0].productPayment.name
        MoneyHelper.setRupiah(tv_price_payment, responseGetDataPayment.data[0].productPayment.price)
        MoneyHelper.setRupiah(tv_total_price_payment, checkedAmount)
        tv_total_qty_payment.text = responseGetDataPayment.data[0].quantity.toString()
        MoneyHelper.setRupiah(tv_ongkir_payment, ongkir)
        MoneyHelper.setRupiah(tv_total_payment, transactionAmount)
        if (responseGetDataPayment.data[0].productPayment.product_discont_present!=0){
            tv_discountqty_payment.text = responseGetDataPayment.data[0].productPayment.product_discont_present.toString()+"%"
            tv_discountqty_payment.visibility = View.VISIBLE
            textView11.visibility = View.VISIBLE
        }


    }

    override fun onResultUploadPhoto(photo_payment: String) {
        photo_id = photo_payment
        btn_submit_payment.visibility = View.VISIBLE
    }

    override fun resultPayment(sukses: Boolean) {
        if (sukses) {
            finish()
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun uploadPhoto() {
        upload_foto_payment.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            ImagePicker.with(this@PaymentActivity)
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
                upload_foto_payment.setImageURI(fileUri)
                val file: File = ImagePicker.getFile(data)!!
                paymentPresenter.uploadFoto(sessionManager.prefToken, file)

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
        val bottomSheetDialog = DialogHelper.bottomSheetDialogTransaksi(this)
        bottomSheetDialog.show()
    }
}