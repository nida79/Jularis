package com.ekr.jularis.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.histori.HistoriData

import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import kotlinx.android.synthetic.main.item_histori.view.*

class  TransactionAdapter(private var historiData: ArrayList<HistoriData>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener


    interface OnItemClickListener {
        fun onItemClick(position: Int, data: HistoriData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        notifyDataSetChanged()
        mListener = listener
    }

    fun setData(firstResult: List<HistoriData>) {
        historiData.clear()
        historiData.addAll(firstResult)
        notifyDataSetChanged()
    }

    fun setNextData(nextResult: List<HistoriData>) {
        historiData.addAll(nextResult)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_histori,
            parent,
            false
        )
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historiData[position])

    }

    override fun getItemCount() = historiData.size

    class ViewHolder(itemView: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var lastposition = -1
        fun bind(historiData: HistoriData) {
            with(itemView) {
                val position = adapterPosition
                GlideHelper.setImage(
                    context,
                    historiData.pictureTransaction,
                    img_item_histori
                )
                MoneyHelper.setRupiah(tv_price_histori, historiData.transactionAmount)
                tv_state_histori.text = historiData.transactionState
                tv_invoice_histori.text = historiData.transactionInvoice
                tv_tgl_histori.text = historiData.transactionDate
                setAnimation(itemView, position, context)
                setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, historiData)
                    }

                }
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