package com.ekr.jularis.ui.activity.detail

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataImageProduct
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.ui.activity.CheckoutActivity
import com.ekr.jularis.ui.activity.login.LoginActivity
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*


class DetailActivity : AppCompatActivity(), DetailContract.View {
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var detailPresenter: DetailPresenter
    private lateinit var dialog: Dialog
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailPresenter = DetailPresenter(this)

    }

    override fun initListener() {
        sessionManager = SessionManager(this)
        dialog = Dialog(this)
        val terimaGambar = intent.extras!!.getParcelableArrayList<DataImageProduct>("image")
        val dataProduct = intent.extras!!.getParcelable<DataProduct>("data")
        detailAdapter =
            terimaGambar?.let { dataProduct?.let { it1 -> DetailAdapter(this, it, it1) } }!!
        img_slider.setSliderAdapter(detailAdapter)
        img_slider.setIndicatorAnimation(IndicatorAnimations.WORM)
        img_slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        img_slider.setOnIndicatorClickListener { position ->
            img_slider.currentPagePosition = position
        }
        if (dataProduct != null) {
            tv_detail_name.text = dataProduct.name
            tv_detail_stok.text = dataProduct.quantity.toString()
            tv_detail_deskripsi.text = dataProduct.description
            MoneyHelper.setRupiah(tv_detail_harga, dataProduct.price)
            btn_detail_addcart.setOnClickListener {
                if (sessionManager.prefIsLogin) {
                    detailPresenter.doAddCart(sessionManager.prefToken, dataProduct.product_id, 1)
                }else{
                    startActivity(Intent(this, LoginActivity::class.java))
                }

            }
            btn_detail_buy.setOnClickListener {
                if (sessionManager.prefIsLogin) {
                    detailPresenter.doBuy(this,sessionManager.prefToken, dataProduct.product_id, 1)

                }else{
                    startActivity(Intent(this, LoginActivity::class.java))
                }

            }
        }


    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                dialog.setContentView(R.layout.loading_dialog)
                dialog.setCanceledOnTouchOutside(false)
                Objects.requireNonNull(dialog.window)!!.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
                )
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
                dialog.setCancelable(true)
                dialog.show()
            }
            false -> {
                dialog.dismiss()
            }
        }
    }

}