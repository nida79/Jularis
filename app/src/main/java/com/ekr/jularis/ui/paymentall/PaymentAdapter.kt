package com.ekr.jularis.ui.paymentall

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.payment.DataGetPayment
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import kotlinx.android.synthetic.main.item_payment.view.*

class PaymentAdapter(private var dataGetPayment: ArrayList<DataGetPayment>) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(dataGetPayment[position],dataGetPayment[position].picturePayment.picture)
    }

    override fun getItemCount() = dataGetPayment.size

    fun setData(firstResult: List<DataGetPayment>) {
        dataGetPayment.clear()
        dataGetPayment.addAll(firstResult)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(dataGetPayment: DataGetPayment, picture: String) {
           with(itemView){
               GlideHelper.setImage(context,picture,img_item_payment)
               tv_title_payment.text = dataGetPayment.productPayment.name
               MoneyHelper.setRupiah(tv_price_payment,dataGetPayment.productPayment.price)
               tv_qty_payment.text = "x ${dataGetPayment.quantity}"
           }
        }

    }

}