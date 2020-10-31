package com.ekr.jularis.ui.admin.employee

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.employee.EmployeeData
import com.ekr.jularis.utils.GlideHelper
import kotlinx.android.synthetic.main.employee_list.view.*

class EmployeeAdapter(private var employeeData: ArrayList<EmployeeData>) :
    RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: EmployeeData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        notifyDataSetChanged()
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.employee_list,
            parent,
            false
        )
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(employeeData[position])
    }

    fun setData(firstResult: List<EmployeeData>) {
        employeeData.clear()
        employeeData.addAll(firstResult)
        notifyDataSetChanged()
    }

    fun setNextData(nextResult: List<EmployeeData>) {
        employeeData.addAll(nextResult)
        notifyDataSetChanged()
    }

    override fun getItemCount() = employeeData.size

    class ViewHolder(itemView: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var lastposition = -1
        private var aktif = "Offline"
        fun bind(employeeData: EmployeeData) {
            with(itemView) {
                val position = adapterPosition
                tv_name_employee.text = employeeData.name
                tv_email_employee.text = employeeData.email
                if (employeeData.isOnline!=0) {
                    aktif = "Online"
                    tv_online_employee.text = aktif
                }else{
                    ViewCompat.setBackgroundTintList(
                        tv_online_employee,
                        ColorStateList.valueOf(resources.getColor(R.color.abu_soft)))
                    tv_online_employee.text = aktif
                }


                GlideHelper.setImage(context, employeeData.photo, img_employee)
                setAnimation(itemView, position, context)
                setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, employeeData)
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