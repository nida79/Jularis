package com.ekr.jularis.ui.activity.detail

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.pop_up_detail.*
import java.util.*


class DetailActivity : AppCompatActivity(), DetailContract.View {
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var detailPresenter: DetailPresenter
    private lateinit var dialog: Dialog
    private lateinit var dataProduct : DataProduct
    private lateinit var sessionManager: SessionManager
    private var qty = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailPresenter = DetailPresenter(this)
        sessionManager = SessionManager(this)
        dialog = Dialog(this)
    }

    override fun initListener() {
        val terimaGambar = intent.extras!!.getParcelableArrayList<DataImageProduct>("image")
        dataProduct = intent.extras!!.getParcelable("data")!!
        detailAdapter =
            terimaGambar?.let { dataProduct?.let { it1 -> DetailAdapter(this, it, it1) } }!!
        img_slider.setSliderAdapter(detailAdapter)
        img_slider.setIndicatorAnimation(IndicatorAnimations.WORM)
        img_slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        img_slider.setOnIndicatorClickListener { position ->
            img_slider.currentPagePosition = position
        }
            tv_detail_name.text = dataProduct.name
            tv_detail_stok.text = dataProduct.quantity.toString()
            tv_detail_deskripsi.text = dataProduct.description
            MoneyHelper.setRupiah(tv_detail_harga, dataProduct.price)


    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                spin_kit_detail.visibility = View.VISIBLE
                btn_detail_addcart.visibility = View.GONE
                btn_detail_buy.visibility = View.GONE
            }
            false -> {
                spin_kit_detail.visibility = View.GONE
                btn_detail_addcart.visibility = View.VISIBLE
                btn_detail_buy.visibility = View.VISIBLE
            }
        }
    }

    override fun resultCounter(int: Int) {
        qty = int
        dialog.pop_up_tv_result.text = qty.toString()
        if (qty<=1){
            dialog.pop_up_min.visibility = View.GONE
        }else{
            dialog.pop_up_min.visibility = View.VISIBLE
        }
    }

    override fun actionButton() {
        btn_detail_buy.setOnClickListener {
            if (sessionManager.prefIsLogin) {
                dialog.setContentView(R.layout.pop_up_detail)
                dialog.setCanceledOnTouchOutside(false)
                Objects.requireNonNull(dialog.window)!!.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager
                        .LayoutParams.WRAP_CONTENT
                )
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
                dialog.setCancelable(true)
                dialog.show()
                dialog.pop_up_pls.setOnClickListener {
                    detailPresenter.doCalculatePlus(qty)
                }
                dialog.pop_up_min.setOnClickListener {
                    detailPresenter.doCalculateMinus(qty)
                }
                dialog.pop_up_cancel_detail.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.pop_up_submit.setOnClickListener {
                    detailPresenter.doBuy(
                        this,
                        sessionManager.prefToken,
                        dataProduct.product_id,
                        qty
                    )
                    dialog.dismiss()
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }
        btn_detail_addcart.setOnClickListener {
            if (sessionManager.prefIsLogin) {
                detailPresenter.doAddCart(sessionManager.prefToken, dataProduct.product_id, 1)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }
    }

}