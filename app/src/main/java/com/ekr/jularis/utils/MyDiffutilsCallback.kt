package com.ekr.jularis.utils

import androidx.recyclerview.widget.DiffUtil
import com.ekr.jularis.data.product.DataProduct

class MyDiffutilsCallback(
    private val oldList: List<DataProduct>,
    private val newList: List<DataProduct>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
      return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}