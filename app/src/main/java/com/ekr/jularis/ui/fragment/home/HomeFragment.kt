package com.ekr.jularis.ui.fragment.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.product.ResponseImage
import com.ekr.jularis.data.product.ResponseProduct
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject


class HomeFragment : Fragment() {
    private lateinit var homeAdapter: HomeAdapter
    lateinit var grid_manager: GridLayoutManager
    private val dataProduct = ArrayList<DataProduct>()
    private val dataImage = ArrayList<ResponseImage>()
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var total_page: Int = 0


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidNetworking.initialize(context)
        initListener()
        getProduct()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    private fun nextgetProduct() {
        showLoading(true)
        AndroidNetworking.get("http://103.55.36.171:8001/v1/product")
            .addQueryParameter("page", page.toString())
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    hideLoading()
                    swp_product_user.isRefreshing = false
                    total_page = response!!.getInt("last_page")
                    val jsonArray = response.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val data =
                            Gson().fromJson(jsonObject.toString(), DataProduct::class.java)
                        dataProduct.addAll(listOf(data))
                        val arrayImage = jsonObject.getJSONArray("product_picture")
                        for (picture in 0 until arrayImage.length()) {
                            val imageObject = arrayImage.getJSONObject(picture)
                            val listImage =
                                Gson().fromJson(imageObject.toString(), ResponseImage::class.java)
                            dataImage.addAll(listOf(listImage))
                        }
                    }
                    homeAdapter.notifyDataSetChanged()

                }

                override fun onError(anError: ANError?) {
                    swp_product_user.isRefreshing = false
                    showLoading(false)
                    Toast.makeText(context, anError!!.errorBody.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun getProduct() {
        onLoading(true)
        AndroidNetworking.get("http://103.55.36.171:8001/v1/product")
            .addQueryParameter("page", page.toString())
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    dataImage.clear()
                    dataProduct.clear()
                    onLoading(false)
                    swp_product_user.isRefreshing = false
                    total_page = response!!.getInt("last_page")
                    val jsonArray = response.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val data =
                            Gson().fromJson(jsonObject.toString(), DataProduct::class.java)
                        dataProduct.addAll(listOf(data))
                        val arrayImage = jsonObject.getJSONArray("product_picture")
                        for (picture in 0 until arrayImage.length()) {
                            val imageObject = arrayImage.getJSONObject(picture)
                            val listImage =
                                Gson().fromJson(imageObject.toString(), ResponseImage::class.java)
                            dataImage.addAll(listOf(listImage))
                        }
                    }
                    homeAdapter.notifyDataSetChanged()

                }

                override fun onError(anError: ANError?) {
                    onLoading(false)
                    Toast.makeText(context, anError!!.errorBody.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun initListener() {
        homeAdapter = HomeAdapter(requireContext(), dataProduct)
        grid_manager = if (requireActivity().resources.configuration.orientation
            == Configuration.ORIENTATION_PORTRAIT
        ) {
            GridLayoutManager(activity, 2)
        } else {
            GridLayoutManager(activity, 4)
        }
        rcv_product_user.apply {
            setHasFixedSize(true)
            layoutManager = grid_manager
            adapter = homeAdapter
        }
        swp_product_user.setOnRefreshListener {
            page = 1
            progress_bar_horizontal_home.visibility = View.GONE
            getProduct()
        }
        rcv_product_user.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val countItem = grid_manager.itemCount
                val lastVisiblePosition =
                    grid_manager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && page < total_page) {
                    showLoading(true)
                    page = page.plus(1)
                    nextgetProduct()
                }
            }
        })
    }

    private fun showLoading(isRefresh: Boolean) {
        isLoading = true
        swp_product_user.isRefreshing = true
        progress_bar_horizontal_home.visibility = View.VISIBLE
        rcv_product_user.visibility.let {
            if (isRefresh) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                shimmer_container.showShimmer()
                rcv_product_user.visibility = View.GONE
                shimmer_container.visibility = View.VISIBLE
                shimmer_container.shimmerLayoutManager = grid_manager
                progress_bar_horizontal_home.visibility = View.GONE
                swp_product_user.isRefreshing = true
            }
            false -> {
                shimmer_container.hideShimmer()
                rcv_product_user.visibility = View.VISIBLE
                shimmer_container.visibility = View.GONE
                swp_product_user.isRefreshing = false
                progress_bar_horizontal_home.visibility = View.GONE
            }
        }
    }

    fun onResultProduct(responseProduct: ResponseProduct) {
        val dataProduct: List<DataProduct>? = responseProduct.data
        total_page = responseProduct.last_page!!
        dataProduct?.let { homeAdapter.setData(it) }
    }

    fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
        isLoading = false
        progress_bar_horizontal_home.visibility = View.GONE
        rcv_product_user.visibility = View.VISIBLE
    }
}