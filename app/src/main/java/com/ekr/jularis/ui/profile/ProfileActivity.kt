package com.ekr.jularis.ui.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ekr.jularis.R
import com.ekr.jularis.data.profile.DataGet
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.SessionManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.layout_edt_profile.*
import java.io.File

class ProfileActivity : AppCompatActivity(), ProfileContract.View {
    private lateinit var profilePresenter: ProfilePresenter
    private lateinit var sessionManager: SessionManager
    private lateinit var loadingDialog: Dialog
    private lateinit var editDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        sessionManager = SessionManager(this)
        loadingDialog = DialogHelper.globalLoading(this)
        profilePresenter = ProfilePresenter(this)

        editDialog = DialogHelper.editDialog(this)
        tv_email_profile.text = sessionManager.prefEmail
        tv_usrname_profile.text = sessionManager.prefUsername
        tv_name_profile.text  = sessionManager.prefFullname
        tv_address_profile.text   = sessionManager.prefAlamat
        tv_nohp_profile.text  = sessionManager.prefNohp
        GlideHelper.setImage(this, sessionManager.prefFoto, img_edit_profile)
    }

    override fun initListener() {
        button2.setOnClickListener {
            finish()
        }
        button_show_dialog.setOnClickListener {
            editDialog.show()
            editDialog.edit_fullname.setText(sessionManager.prefFullname)
            editDialog.edit_alamat.setText(sessionManager.prefAlamat)
            editDialog.edit_nohp.setText(sessionManager.prefNohp)
            GlideHelper.setImage(this,sessionManager.prefFoto , editDialog.img_edit_profile_dilaog)
            editDialog.btn_simpan_edit.setOnClickListener {
                profilePresenter.doUpdateProfile(
                    sessionManager.prefToken,
                    editDialog.edit_fullname.text.toString(),
                    editDialog.edit_nohp.text.toString(),
                    editDialog.edit_alamat.text.toString()
                )
            }
            editDialog.edit_close.setOnClickListener {
                editDialog.dismiss()
            }
            uploadPhoto()
        }
    }

    override fun resultUpdate(status: Boolean) {
       if (status){
           editDialog.dismiss()
           profilePresenter.getProfile(sessionManager.prefToken)
       }
    }

    override fun resultUpload(status: Boolean) {
        if (status){
            profilePresenter.getProfile(sessionManager.prefToken)
        }
    }

    override fun resultGetProfile(dataGet: DataGet) {
        GlideHelper.setImage(this, dataGet.photo, img_edit_profile)
        sessionManager.prefFullname = dataGet.fullName
        sessionManager.prefAlamat = dataGet.address
        sessionManager.prefNohp = dataGet.noTelp
        sessionManager.prefFoto = dataGet.photo
        tv_name_profile.text = dataGet.fullName
        tv_address_profile.text = dataGet.address
        tv_nohp_profile.text = dataGet.noTelp

    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                loadingDialog.show()
            }
            false -> {
                loadingDialog.dismiss()
            }
        }
    }

    private fun uploadPhoto() {
        editDialog.btn_upload_foto_profile.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            ImagePicker.with(this@ProfileActivity)
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
                editDialog.img_edit_profile_dilaog.setImageURI(fileUri)
                val file: File = ImagePicker.getFile(data)!!
                profilePresenter.doUploadFoto(sessionManager.prefToken, file)

            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Batal Mengambil Gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}