package com.ekr.jularis.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataImageProduct
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.utils.GlideHelper
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.list_item_slider.view.*

class DetailAdapter(val context: Context, private var dataImageProduct: ArrayList<DataImageProduct>,val dataProduct: DataProduct) :
    SliderViewAdapter<DetailAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup?) = SliderAdapterVH(
        LayoutInflater.from(parent?.context).inflate(R.layout.list_item_slider, parent, false)
    )

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        viewHolder?.bind(dataImageProduct[position],dataProduct)
    }

    override fun getCount(): Int = dataImageProduct.size

    class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        fun bind(dataImageProduct: DataImageProduct, dataProduct: DataProduct) {
            with(itemView) {
                GlideHelper.setImage(context, dataImageProduct.picture, imgAutoSlider)
                tvDescSlider.text = dataProduct.name
            }
        }
    }
}