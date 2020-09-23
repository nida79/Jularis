package com.ekr.jularis.ui.fragment.home

import android.content.Intent
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
import com.ekr.jularis.R
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.response.ResponseProduct
import com.ekr.jularis.ui.activity.home.MainActivity
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), HomeContract.View {
    private lateinit var homeAdapter: HomeAdapter
    lateinit var gridManager: GridLayoutManager
    private lateinit var homePresenter: HomePresenter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPage: Int = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidNetworking.initialize(context)
        homePresenter = HomePresenter(this)
        homePresenter.getProduct(page)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun initListener() {
        homeAdapter = HomeAdapter(requireContext(), arrayListOf())
        gridManager = if (requireActivity().resources.configuration.orientation
            == Configuration.ORIENTATION_PORTRAIT
        ) {
            GridLayoutManager(activity, 2)
        }
        else {
            GridLayoutManager(activity, 4)
        }
        rcv_product_user.apply {
            setHasFixedSize(true)
            layoutManager = gridManager
            adapter = homeAdapter
        }
        swp_product_user.setOnRefreshListener {
            page = 1
            homePresenter.getProduct(page)
        }
        rcv_product_user.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val countItem = gridManager.itemCount
                val lastVisiblePosition =
                    gridManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && page < totalPage) {
                    page = page.plus(1)
                    homePresenter.getNextProduct(page)
                }
            }
        })
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                swp_product_user.isRefreshing = true
                shimmer_container.visibility = View.VISIBLE
                shimmer_container.startShimmer()
                rcv_product_user.visibility = View.GONE
            }
            false -> {
                swp_product_user.isRefreshing = false
                shimmer_container.stopShimmer()
                shimmer_container.visibility = View.GONE
                rcv_product_user.visibility = View.VISIBLE
            }
        }
    }

    override fun onNextLoading(nextLoading: Boolean) {
        when (nextLoading) {
            true -> {
                isLoading = true
                progress_bar_horizontal_home.visibility = View.VISIBLE
            }
            false -> {
                isLoading = false
                progress_bar_horizontal_home.visibility = View.GONE
            }
        }
    }

    override fun onResultProduct(responseProduct: ResponseProduct) {
        val data: List<DataProduct>? = responseProduct.data
        totalPage = responseProduct.last_page!!
        data?.let { homeAdapter.setData(it) }
    }

    override fun onResultNextPage(responseProduct: ResponseProduct) {
        val data: List<DataProduct>? = responseProduct.data
        totalPage = responseProduct.last_page!!
        data?.let { homeAdapter.setNextData(it) }
    }
    override fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}