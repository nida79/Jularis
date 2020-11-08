package com.ekr.jularis.ui.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataImageProduct
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.ui.login.LoginActivity
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.dialog_count_product.*


class DetailActivity : AppCompatActivity(), DetailContract.View {
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var detailPresenter: DetailPresenter
    private lateinit var dialog: Dialog
    private lateinit var dataProduct : DataProduct
    private lateinit var sessionManager: SessionManager
    private var qty = 1
    private var jumlahProduk = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailPresenter = DetailPresenter(this)
        sessionManager = SessionManager(this)
        dialog = Dialog(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        val terImage = intent.extras!!.getParcelableArrayList<DataImageProduct>("image")
        dataProduct = intent.extras!!.getParcelable("data")!!
        btn_whatsapp.setOnClickListener {
            val pm = packageManager
            try {
                val info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
                if (info!=null){
                    val phoneNumberWithCountryCode = "+6281220168871"
                    val message = "Hallo Admin Laris-App, Saya butuh bantuan\n"
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                String.format(
                                    "https://api.whatsapp.com/send?phone=%s&text=%s",
                                    phoneNumberWithCountryCode, message
                                )
                            )
                        )
                    )
                }

            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        btn_detail_back.setOnClickListener {
            finish()
        }
        detailAdapter =
            terImage?.let { DetailAdapter(this, it, dataProduct) }!!
        img_slider.setSliderAdapter(detailAdapter)
        img_slider.setIndicatorAnimation(IndicatorAnimations.WORM)
        img_slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        img_slider.setOnIndicatorClickListener { position ->
            img_slider.currentPagePosition = position
        }
           jumlahProduk =dataProduct.quantity
            tv_detail_name.text = dataProduct.name
            tv_detail_stok.text = dataProduct.quantity.toString()
            tv_detail_deskripsi.text = dataProduct.description
            MoneyHelper.setRupiah(tv_detail_harga, dataProduct.price)
        if (dataProduct.product_discont_present!=0){
            tv_potongan.visibility=View.VISIBLE
            detail_potongan.visibility=View.VISIBLE
            detail_potongan.text = "Potongan ${dataProduct.product_discont_present}% untuk pemebelian Lebih dari ${dataProduct.product_discont_quantity} produk"
        }


    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                spin_kit_detail.visibility = View.VISIBLE
                btn_detail_addcart.visibility = View.GONE
                btn_detail_back.visibility = View.GONE
                btn_detail_buy.visibility = View.GONE
            }
            false -> {
                spin_kit_detail.visibility = View.GONE
                btn_detail_addcart.visibility = View.VISIBLE
                btn_detail_buy.visibility = View.VISIBLE
                btn_detail_back.visibility = View.VISIBLE
            }
        }
    }



    override fun actionButton() {
        btn_detail_buy.setOnClickListener {
            if (sessionManager.prefIsLogin) {
                dialog.setContentView(R.layout.dialog_count_product)
                dialog.setCanceledOnTouchOutside(false)
                dialog.window!!.setLayout(
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
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("logout", "detail")
                startActivity(intent)
            }

        }
    }
    override fun resultCounter(int: Int) {
        qty = int
        dialog.pop_up_tv_result.text = qty.toString()
        when {
            qty <= 1 -> {
                dialog.pop_up_min.visibility = View.GONE
            }
            qty == jumlahProduk -> {
                dialog.pop_up_pls.visibility = View.GONE
            }
            else -> {
                dialog.pop_up_min.visibility = View.VISIBLE
            }
        }
    }
}