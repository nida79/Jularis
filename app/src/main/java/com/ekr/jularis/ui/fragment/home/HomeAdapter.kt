package com.ekr.jularis.ui.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.product.ResponseImage
import com.ekr.jularis.utils.GlideHelper
import kotlinx.android.synthetic.main.product_item.view.*

class HomeAdapter(private val context: Context, var dataProduct: ArrayList<DataProduct>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataProduct[position])
    }

    override fun getItemCount() = dataProduct.size

    fun setData(newDataProduct: List<DataProduct>) {
        dataProduct.clear()
        dataProduct.addAll(newDataProduct)
        notifyDataSetChanged()
    }

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data: DataProduct) {
            with(itemView) {
                tv_card_title.text = data.name
                tv_card_price.text = data.price.toString()
                tv_item_sold.text = "Terjual("+data.quantity.toString()+")"

            }

        }
    }

}