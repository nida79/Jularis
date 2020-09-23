package com.ekr.jularis.utils

import android.widget.TextView
import java.text.NumberFormat
import java.util.*

class MoneyHelper {
    companion object{
        fun setRupiah(textView: TextView,int: Int){
            val localeID = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            formatRupiah.maximumFractionDigits = 0
            textView.text = formatRupiah.format(int).toString()

        }
    }

}