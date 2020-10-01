package com.ekr.jularis.ui.payment

import android.Manifest
import android.app.Activity
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
import com.ekr.jularis.data.response.ResponseGetDataPayment
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_payment.*
import java.io.File

class PaymentActivity : AppCompatActivity(), PaymentContract.View {
    private lateinit var tipeBayar: String
    private lateinit var sessionManager: SessionManager
    private lateinit var paymentPresenter: PaymentPresenter
    private lateinit var product_id: String
    private lateinit var photo_id: String
    private var alamat = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        paymentPresenter = PaymentPresenter(this)
        sessionManager = SessionManager(this)
        product_id = intent.getStringExtra("pd_id").toString()
        paymentPresenter.getDataPayment(sessionManager.prefToken, product_id)
        edt_alamat_payment.setText(sessionManager.prefAlamat)

    }

    override fun initListener() {


        radioGroup.setOnCheckedChangeListener { _, _ ->
            radioSelected()
        }
        uploadPhoto()
    }

    override fun radioSelected() {
        if (bayar_transfer.isChecked) {
            tipeBayar = bayar_transfer.text.toString()
            upload_foto_payment.visibility = View.VISIBLE
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
            btn_submit_payment.visibility = View.VISIBLE
        }
        if (bayar_ditempat.isChecked) {
            upload_foto_payment.visibility = View.GONE
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
            btn_submit_payment.visibility = View.VISIBLE
        }
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> progress_bar_horizontal_payment.visibility = View.VISIBLE
            false -> progress_bar_horizontal_payment.visibility = View.GONE
        }
    }

    override fun loadingFoto(loadingFoto: Boolean) {
        when (loadingFoto) {
            true -> progress_bar_horizontal_payment.visibility = View.VISIBLE
            false -> progress_bar_horizontal_payment.visibility = View.GONE
        }
    }

    override fun onResultDataPayment(responseGetDataPayment: ResponseGetDataPayment) {
        GlideHelper.setImage(
            applicationContext,
            responseGetDataPayment.data[0].picturePayment.picture, img_item_payment
        )
        tv_title_payment.text = responseGetDataPayment.data[0].productPayment.name
        tv_qty_payment.text = responseGetDataPayment.data[0].quantity.toString()
        MoneyHelper.setRupiah(tv_price_payment, responseGetDataPayment.data[0].productPayment.price)
        MoneyHelper.setRupiah(tv_total_price_payment, responseGetDataPayment.checkedAmount)
        tv_total_qty_payment.text = responseGetDataPayment.count.toString()
        MoneyHelper.setRupiah(tv_ongkir_payment, responseGetDataPayment.ongkir)
        MoneyHelper.setRupiah(tv_total_payment, responseGetDataPayment.transactionAmount)

    }

    override fun onResultUploadPhoto(photo_payment: String) {
        photo_id = photo_payment
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
}