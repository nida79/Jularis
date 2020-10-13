package com.ekr.jularis.ui.admin.product.mange

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.ui.admin.product.ImagePickerAdapter
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.SessionManager
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.ImageQuality
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_product_manage.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import java.io.File
import java.util.*

class ProductActivityManage : AppCompatActivity(), ProductManageContract.View {
    private lateinit var productPresenter: ProductManagePresenter
    private lateinit var sessionManager: SessionManager
    private  var dataProduct: DataProduct? = null
    private var returnValue = ArrayList<String>()
    private val mFile = ArrayList<File>()
    private lateinit var options: Options
    private lateinit var dialog: Dialog
    private lateinit var imagePickerAdapter: ImagePickerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_manage)
        dialog = DialogHelper.globalLoading(this)
        productPresenter = ProductManagePresenter(this, applicationContext)
        sessionManager = SessionManager(this)
        options = Options.init()
            .setRequestCode(100)
            .setCount(5)
            .setFrontfacing(false)
            .setImageQuality(ImageQuality.REGULAR)
            .setPreSelectedUrls(returnValue)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/jularis")

    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        dataProduct = intent.extras?.getParcelable("data")
        imagePickerAdapter = ImagePickerAdapter(arrayListOf())
        if (dataProduct!=null){
            edit_nama_product.setText(dataProduct!!.name)
            edit_price_product.setText(dataProduct!!.price.toString())
            edit_description_product.setText(dataProduct!!.description)
            edit_qty_product.setText(dataProduct!!.quantity.toString())
            edit_ongkir_product.setText(dataProduct!!.ongkir)

            btn_update_produk.setOnClickListener {
                productPresenter.doUpdateProduct(
                    sessionManager.prefToken,
                    dataProduct!!.product_id,
                    edit_nama_product.text.toString(),
                    edit_price_product.text.toString(),
                    edit_description_product.text.toString(),
                    spinner_update.selectedItem.toString(),
                    edit_qty_product.text.toString(),
                    edit_ongkir_product.text.toString(),
                    mFile
                )
            }
            img_delete_produk.setOnClickListener {
                val peringatan = DialogHelper.bottomSheetDialogDelete(this)
                peringatan.show()
                peringatan.btn_sheet_ok.setOnClickListener {
                    productPresenter.doDeleteProduct(sessionManager.prefToken, dataProduct!!.product_id)
                    peringatan.dismiss()
                }

            }
        }
        else{
            img_delete_produk.visibility = View.GONE
            header_manage_product.text = "Tambah Produk"
            btn_update_produk.setOnClickListener {
                when{
                    edit_nama_product.text.toString().isEmpty() ->{
                        edit_nama_product.error = "Nama Produk Tidak Boleh Kosong"
                        edit_nama_product.requestFocus()
                    }
                    edit_price_product.text.toString().isEmpty() ->{
                        edit_price_product.error = "Harga Tidak Boleh Kosong"
                        edit_price_product.requestFocus()
                    }
                    edit_description_product.text.toString().isEmpty() ->{
                        edit_description_product.error = "Deskripsi Tidak Boleh Kosong"
                        edit_description_product.requestFocus()
                    }
                    edit_qty_product.text.toString().isEmpty() ->{
                        edit_qty_product.error = "Jumlah Produk Tidak Boleh Kosong"
                        edit_qty_product.requestFocus()
                    }
                    edit_ongkir_product.text.toString().isEmpty() ->{
                        edit_ongkir_product.error = "Ongkos Kirim Tidak Boleh Kosong"
                        edit_ongkir_product.requestFocus()
                    }
                    mFile.isEmpty()->{
                        Toasty.warning(applicationContext,"Foto Belum Ditambahkan",Toasty.LENGTH_SHORT).show()
                    }
                    else->{
                        productPresenter.doAddProduct(
                            sessionManager.prefToken,
                            edit_nama_product.text.toString(),
                            edit_price_product.text.toString(),
                            edit_description_product.text.toString(),
                            spinner_update.selectedItem.toString(),
                            edit_qty_product.text.toString(),
                            edit_ongkir_product.text.toString(),
                            mFile
                        )
                    }
                }

            }

        }
        update_pick_image.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            options.preSelectedUrls = returnValue
                            Pix.start(this@ProductActivityManage, options)
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
        rcv_image_update.apply {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            adapter = imagePickerAdapter
        }
        indicator_update.attachToRecyclerView(rcv_image_update)
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

    override fun resultUpdateProduct(hasil: Boolean) {
        if (hasil) {
            finish()
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

    override fun showMessage(message: String) {
        Toasty.success(applicationContext, message, Toasty.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)!!
                imagePickerAdapter.addImage(returnValue)
                for (i in returnValue.indices) {
                    mFile.add(File(returnValue[i]))
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}