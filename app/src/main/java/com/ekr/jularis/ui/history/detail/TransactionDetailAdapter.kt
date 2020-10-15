package com.ekr.jularis.ui.history.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.response.HistoriNewProduct
import com.ekr.jularis.utils.GlideHelper
import kotlinx.android.synthetic.main.item_payment.view.*

class TransactionDetailAdapter(private var data: ArrayList<HistoriNewProduct>) :
    RecyclerView.Adapter<TransactionDetailAdapter.ViewHolder>() {

    fun setData(firstResult: List<HistoriNewProduct>) {
        data.clear()
        data.addAll(firstResult)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            data[position]
        )
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(historiNewProduct: HistoriNewProduct) {
            with(itemView){
                GlideHelper.setImage(context,historiNewProduct.productPicture,img_item_payment)
                tv_title_payment.text = historiNewProduct.productName
                tv_qty_payment.text = "x ${historiNewProduct.quantity}"
            }
        }

    }
}