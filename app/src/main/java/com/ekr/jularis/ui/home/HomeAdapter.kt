package com.ekr.jularis.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataImageProduct
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.MyDiffutilsCallback
import kotlinx.android.synthetic.main.item_product.view.*

class HomeAdapter(private var dataProduct: ArrayList<DataProduct>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int,data: DataProduct)
        fun onButtonClick(position: Int,data: DataProduct)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        notifyDataSetChanged()
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_product,
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

    class ViewHolder(itemView: View, private val listener: OnItemClickListener)
        : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data: DataProduct, productPicture: List<DataImageProduct>) {
            with(itemView) {
                val position = adapterPosition
                tv_card_title.text = data.name
                MoneyHelper.setRupiah(tv_card_price, data.price)
                tv_item_sold.text = "Stok (" + data.quantity.toString() + ")"
                GlideHelper.setImage(context, productPicture[0].picture, iv_card_product)
                btn_buy_home.setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onButtonClick(position,data)
                    }
                }
                setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position,data)
                    }

                }
            }
        }
    }
}