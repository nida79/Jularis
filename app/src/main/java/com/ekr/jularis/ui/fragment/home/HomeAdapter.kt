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
import java.text.NumberFormat
import java.util.*

class HomeAdapter(
    private val context: Context,
    private var dataProduct: ArrayList<DataProduct>
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataProduct[position], dataProduct[position].product_picture)
    }

    override fun getItemCount() = dataProduct.size

    fun setData(firstResult: List<DataProduct>) {
        dataProduct.clear()
        dataProduct.addAll(firstResult)
        notifyDataSetChanged()
    }
    fun setNextData(nextResult: List<DataProduct>) {
        dataProduct.addAll(nextResult)
        notifyDataSetChanged()
    }

    class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var localeID: Locale = Locale("in", "ID")
        private var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        @SuppressLint("SetTextI18n")
        fun bind(data: DataProduct, productPicture: List<ResponseImage>) {
            with(itemView) {
                formatRupiah.maximumFractionDigits = 0
                tv_card_title.text = data.name
                tv_card_price.text = formatRupiah.format(data.price).toString()
                tv_item_sold.text = "Stok (" + data.quantity.toString() + ")"
                GlideHelper.setImage(context, productPicture[0].picture, iv_card_product)

            }
        }
    }

}