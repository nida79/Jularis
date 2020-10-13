package com.ekr.jularis.ui.admin.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.utils.GlideHelper
import kotlinx.android.synthetic.main.image.view.*
import java.util.*

class ImagePickerAdapter(private var arrayList: ArrayList<String>) :
    RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {

    fun addImage(list: ArrayList<String>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.image, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int = arrayList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(s: String) {
            with(itemView) {
                GlideHelper.setImage(context, s, img_update_product)
            }
        }

    }
}