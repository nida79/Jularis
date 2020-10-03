package com.ekr.jularis.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.ekr.jularis.R

class LoadingDialog {
    companion object{
       fun globalLoading(activity: Activity): Dialog {
           val dialog = Dialog(activity)
           dialog.setContentView(R.layout.pop_loading)
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
            dialog.setContentView(R.layout.pop_loading)
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