package com.ekr.jularis.ui.fragment.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.cart.Checkout
import com.ekr.jularis.data.cart.Product
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import kotlinx.android.synthetic.main.checkout_item.view.*

class CartAdapter(val context: Context, private var checkout: ArrayList<Checkout>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    fun setData(data: List<Checkout>) {
        checkout.clear()
        checkout.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.checkout_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(checkout[position], checkout[position].picture.picture,checkout[position].product)

    }

    override fun getItemCount(): Int = checkout.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(checkout: Checkout, picture: String, product: Product) {
            with(itemView) {
                GlideHelper.setImage(context, picture, img_item_keranjang)
                MoneyHelper.setRupiah(tv_price_item_keranjang, product.price)
                tv_title_item_keranjang.text = product.name
                tv_quantity_item_keranjang.text = checkout.quantity.toString()
            }
        }
    }
}