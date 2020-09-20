package com.ekr.jularis.ui.fragment.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.product.ResponseProduct
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), HomeContract.View {
    private lateinit var homePresenter: HomePresenter
    private lateinit var homeAdapter: HomeAdapter
    lateinit var grid_manager: GridLayoutManager
    private var page: Int = 1
    private var total_page: Int = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homePresenter = HomePresenter(this)
        homePresenter.getProduct(page)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun initListener() {
        homeAdapter = HomeAdapter(requireContext(), arrayListOf())
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
            homePresenter.getProduct(page)
        }
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                progress_bar_horizontal_home.visibility = View.VISIBLE
                swp_product_user.isRefreshing = true
            }
            false -> {
                swp_product_user.isRefreshing = false
                progress_bar_horizontal_home.visibility = View.GONE
            }
        }
    }

    override fun onResultProduct(responseProduct: ResponseProduct) {
        val dataProduct: List<DataProduct>? = responseProduct.data
        total_page = responseProduct.last_page!!
        dataProduct?.let { homeAdapter.setData(it) }
    }

    override fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

}