package com.ekr.jularis.ui.fragment.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.cart.Checkout
import com.ekr.jularis.data.cart.Product
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import kotlinx.android.synthetic.main.checkout_item.view.*

class CartAdapter(val context: Context, private var checkout: ArrayList<Checkout>,var boolean: Boolean) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onButtonPlusClick(checkout: Checkout)
        fun onButtonMinusClick( checkout: Checkout)
        fun onCheckBoxClick(checkout: Checkout)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        notifyDataSetChanged()
        mListener = listener
    }

    fun setData(data: List<Checkout>) {
        checkout.clear()
        checkout.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.checkout_item, parent, false)
        return ViewHolder(view, mListener)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            checkout[position],
            checkout[position].picture.picture,
            checkout[position].product,
            boolean
        )

    }
    override fun getItemCount(): Int = checkout.size

    class ViewHolder(itemView: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(checkout: Checkout, picture: String, product: Product, boolean: Boolean) {
            val position = adapterPosition
            var cekLis=boolean
            val centang = checkout.checked
            with(itemView) {
                GlideHelper.setImage(context, picture, img_item_keranjang)
                MoneyHelper.setRupiah(tv_price_item_keranjang, checkout.checkout_amount)
                tv_title_item_keranjang.text = product.name
                tv_quantity_item_keranjang.text = checkout.quantity.toString()
                if (centang == "1"){
                    cekLis = true
                    select_product_keranjang.isChecked = true
                    select_product_keranjang.isSelected = true
                    if (checkout.quantity <= 1) {
                        checkout_plus_item.visibility = View.VISIBLE
                        checkout_minus_item.visibility = View.GONE
                    }else{
                        checkout_plus_item.visibility = View.VISIBLE
                        checkout_minus_item.visibility = View.VISIBLE
                    }
                }else{
                    cekLis = false
                    select_product_keranjang.isChecked = false
                    select_product_keranjang.isSelected = false
                }

                //click event handle
                select_product_keranjang.setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        when(cekLis){
                            false->{
                                cekLis = true
                                checkout_plus_item.visibility = View.VISIBLE
                                if (checkout.quantity <= 1) {
                                    checkout_minus_item.visibility = View.GONE
                                }else{
                                    checkout_minus_item.visibility = View.VISIBLE
                                }
                            }
                            true->{
                                cekLis = false
                                checkout_plus_item.visibility = View.INVISIBLE
                                checkout_minus_item.visibility = View.INVISIBLE

                            }
                        }
                        listener.onCheckBoxClick(checkout)
                    }

                }
                checkout_plus_item.setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onButtonPlusClick(checkout)
                    }
                }
                checkout_minus_item.setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onButtonMinusClick(checkout)
                    }
                }
            }
        }
    }
}