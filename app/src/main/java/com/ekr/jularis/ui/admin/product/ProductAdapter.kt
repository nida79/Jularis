package com.ekr.jularis.ui.admin.product

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataImageProduct
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.MyDiffutilsCallback
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.item_prouct_list.view.*

class ProductAdapter(private var dataProduct: ArrayList<DataProduct>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: DataProduct)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        notifyDataSetChanged()
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_prouct_list,
            parent,
            false
        )
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataProduct[position], dataProduct[position].product_picture)
    }

    override fun getItemCount() = dataProduct.size

    fun updateItem(newList:List<DataProduct>){
        val diffutilsCallback = MyDiffutilsCallback(dataProduct,newList)
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff(diffutilsCallback)
        dataProduct.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
    fun insertItem(newList:List<DataProduct>){
        val diffutilsCallback = MyDiffutilsCallback(dataProduct,newList)
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff(diffutilsCallback)
        dataProduct.clear()
        dataProduct.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setData(firstResult: List<DataProduct>) {
        dataProduct.clear()
        dataProduct.addAll(firstResult)
        notifyDataSetChanged()
    }

    fun setNextData(nextResult: List<DataProduct>) {
        dataProduct.addAll(nextResult)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var lastposition = -1

        @SuppressLint("SetTextI18n")
        fun bind(data: DataProduct, productPicture: List<DataImageProduct>) {
            with(itemView) {
                val position = adapterPosition
                tv_title_product.text = data.name
                MoneyHelper.setRupiah(tv_price_product, data.price)
                tv_qty_product.text = "Stok (" + data.quantity.toString() + ")"
                GlideHelper.setImage(context, productPicture[0].picture, img_item_product)
                setAnimation(itemView, position, context)
                setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, data)
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