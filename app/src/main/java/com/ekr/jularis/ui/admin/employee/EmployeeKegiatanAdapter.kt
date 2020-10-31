package com.ekr.jularis.ui.admin.employee

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.employee.EmployeeData
import com.ekr.jularis.data.employee.KegiatanData
import com.ekr.jularis.ui.history.TransactionAdapter
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import kotlinx.android.synthetic.main.employee_list.view.*
import kotlinx.android.synthetic.main.item_checkout.view.*
import kotlinx.android.synthetic.main.item_histori.view.*
import kotlinx.android.synthetic.main.item_kegiatan_krw.view.*

class EmployeeKegiatanAdapter(private var kegiatanData: ArrayList<KegiatanData>) :
    RecyclerView.Adapter<EmployeeKegiatanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_kegiatan_krw,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(kegiatanData[position])
    }

    override fun getItemCount()= kegiatanData.size

    fun setData(firstResult: List<KegiatanData>) {
        kegiatanData.clear()
        kegiatanData.addAll(firstResult)
        notifyDataSetChanged()
    }

    fun setNextData(nextResult: List<KegiatanData>) {
        kegiatanData.addAll(nextResult)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var lastposition = -1
        fun bind(kegiatanData: KegiatanData) {
            with(itemView){
                val position = adapterPosition
                GlideHelper.setImage(
                    context,
                    kegiatanData.pictureTransaction,
                    img_kegiatan_employee
                )
                MoneyHelper.setRupiah(tv_price_kegiatan, kegiatanData.transactionAmount)
                tv_state_kegiatan.text = kegiatanData.transactionState
                tv_kegiatan_employee.text = kegiatanData.transactionInvoice
                tv_kegiatan_tgl.text = kegiatanData.transactionDate
                setAnimation(itemView, position, context)
            }
        }

        private fun setAnimation(itemView: View, position: Int, context: Context) {
            if (position > lastposition) {
                val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
                itemView.animation = animation
                lastposition = position
            }
        }

    }
}