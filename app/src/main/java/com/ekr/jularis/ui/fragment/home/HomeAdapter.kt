package com.ekr.jularis.ui.fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekr.jularis.R
import com.ekr.jularis.data.ExampleItem
import com.ekr.jularis.data.login.DataLogin
import com.ekr.jularis.utils.GlideHelper
import kotlinx.android.synthetic.main.product_item.view.*

class HomeAdapter(private val context: Context, var exmapleList: List<ExampleItem>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exmapleList[position])
//        GlideHelper.setImage(
//            context,
//            exmapleList[position].imageResource,
//            holder.itemView.iv_card_product
//        )
    }

    override fun getItemCount() = exmapleList.size

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ExampleItem) {
            itemView.tv_card_title.text = data.text1
            itemView.tv_card_price.text = data.text2
            itemView.iv_card_product.setImageResource(data.imageResource)
        }
    }

}