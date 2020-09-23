package com.ekr.jularis.ui.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataImageProduct
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.ui.activity.CheckoutActivity
import com.ekr.jularis.ui.activity.detail.DetailActivity
import com.ekr.jularis.ui.activity.login.LoginActivity
import com.ekr.jularis.utils.GlideHelper
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.product_item.view.*
import kotlin.collections.ArrayList

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var sessionManager: SessionManager
        @SuppressLint("SetTextI18n")
        fun bind(data: DataProduct, productPicture: List<DataImageProduct>) {
            with(itemView) {
                sessionManager = SessionManager(context)
                tv_card_title.text = data.name
                MoneyHelper.setRupiah(tv_card_price,data.price)
                tv_item_sold.text = "Stok (" + data.quantity.toString() + ")"
                GlideHelper.setImage(context, productPicture[0].picture, iv_card_product)
                btn_buy_home.setOnClickListener {
                   if (sessionManager.prefIsLogin){
                       val intent = Intent(context,CheckoutActivity::class.java)
                       context.startActivity(intent)
                   }else{
                       val intent = Intent(context,LoginActivity::class.java)
                       context.startActivity(intent)
                   }
                }
                setOnClickListener {
                    val intent = Intent(context,DetailActivity::class.java)
                    intent.putParcelableArrayListExtra("image", ArrayList(productPicture))
                    intent.putExtra("data",data)
                    context.startActivity(intent)
                }
            }
        }
    }


}