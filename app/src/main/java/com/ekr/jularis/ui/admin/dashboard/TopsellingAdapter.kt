package com.ekr.jularis.ui.admin.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.dashboard.ProductSelling
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.utils.GlideHelper
import kotlinx.android.synthetic.main.item_top_selling.view.*

class TopsellingAdapter(private var productSelling: ArrayList<ProductSelling>) :
    RecyclerView.Adapter<TopsellingAdapter.ViewHolder>() {

    fun setData(firstResult: List<ProductSelling>) {
        productSelling.clear()
        productSelling.addAll(firstResult)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_top_selling, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productSelling[position])
    }

    override fun getItemCount(): Int = productSelling.size

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(productSelling: ProductSelling) {
            with(itemView) {
                GlideHelper.setImage(context, productSelling.product_picture, img_topsel)
                tv_title_top.text = productSelling.productName
                tv_qty_top.text = productSelling.total.toString()
            }
        }

    }
}