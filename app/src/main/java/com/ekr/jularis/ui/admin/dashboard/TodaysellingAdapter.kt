package com.ekr.jularis.ui.admin.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.dashboard.ProductSelling
import com.ekr.jularis.utils.GlideHelper
import kotlinx.android.synthetic.main.item_selling_today.view.*

class TodaysellingAdapter(private var productSelling: ArrayList<ProductSelling>) :
    RecyclerView.Adapter<TodaysellingAdapter.ViewHolder>() {

    fun setData(firstResult: List<ProductSelling>) {
        productSelling.clear()
        productSelling.addAll(firstResult)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_selling_today, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productSelling[position])
    }

    override fun getItemCount(): Int = productSelling.size
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var lastposition = -1
        fun bind(productSelling: ProductSelling) {

            with(view) {
                val position = adapterPosition
                GlideHelper.setImage(context,productSelling.product_picture,img_item_productToday)
                tv_title_productToday.text = productSelling.productName
                tv_qty_productToday.text = productSelling.total.toString()
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