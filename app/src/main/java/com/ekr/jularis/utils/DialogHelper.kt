package com.ekr.jularis.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.ekr.jularis.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.bottom_sheet_logout.*

class DialogHelper {
    companion object {

        fun globalLoading(activity: Activity): Dialog {
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.dialog_global)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager
                    .LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            dialog.setCancelable(true)
            return dialog
        }

        fun successDialog(activity: Activity): Dialog {
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.dialog_global)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager
                    .LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            dialog.setCancelable(true)
            return dialog
        }

        fun changePasswordDialog(activity: Activity): Dialog {
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.change_password)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager
                    .LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            dialog.setCancelable(true)
            return dialog
        }

        fun editDialog(activity: Activity): Dialog {
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.layout_edt_profile)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            dialog.setCancelable(false)
            return dialog
        }

        fun bottomSheetDialogTransaksi(activity: Activity): BottomSheetDialog {
            val bottomSheetDialog = BottomSheetDialog(activity)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
            bottomSheetDialog.setCanceledOnTouchOutside(false)
            bottomSheetDialog.btn_sheet_cancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.btn_sheet_ok.setOnClickListener {
                bottomSheetDialog.dismiss()
                activity.finish()
            }
            return bottomSheetDialog
        }

        fun bottomSheetDialogKonfirm(activity: Activity): BottomSheetDialog {
            val bottomSheetDialog = BottomSheetDialog(activity)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
            bottomSheetDialog.setCanceledOnTouchOutside(false)
            return bottomSheetDialog
        }

        fun bottomSheetDialogUpdate(activity: Activity): BottomSheetDialog {
            val bottomSheetDialog = BottomSheetDialog(activity)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_update)
            bottomSheetDialog.setCanceledOnTouchOutside(false)
            return bottomSheetDialog
        }

        @SuppressLint("SetTextI18n")
        fun bottomSheetDialogDelete(activity: Activity): BottomSheetDialog {
            val bottomSheetDialog = BottomSheetDialog(activity)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
            bottomSheetDialog.text_keterangan.text = "Anda Yakin Ingin Menghapus Produk Ini.?"
            bottomSheetDialog.setCanceledOnTouchOutside(false)
            bottomSheetDialog.btn_sheet_ok.text = "Hapus"
            bottomSheetDialog.btn_sheet_cancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            return bottomSheetDialog
        }

        @SuppressLint("SetTextI18n")
        fun bottomSheetDialogLogout(activity: Activity): BottomSheetDialog {
            val bottomSheetDialog = BottomSheetDialog(activity)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_logout)
            bottomSheetDialog.setCanceledOnTouchOutside(true)
            bottomSheetDialog.btn_sheet_tidak.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            return bottomSheetDialog
        }

        fun ongkirDialog(activity: Activity):Dialog{
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.dialog_set_ongkir)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager
                    .LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            dialog.setCancelable(true)
            return dialog
        }
    }
}