package com.ekr.jularis.ui.fragment.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.response.ResponseProduct
import com.ekr.jularis.databinding.FragmentHomeBinding
import com.ekr.jularis.ui.activity.CheckoutActivity
import com.ekr.jularis.ui.activity.detail.DetailActivity
import com.ekr.jularis.ui.activity.login.LoginActivity
import com.ekr.jularis.utils.SessionManager


class HomeFragment : Fragment(), HomeContract.View {
    private lateinit var _binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var gridManager: GridLayoutManager
    private lateinit var homePresenter: HomePresenter
    private lateinit var data: List<DataProduct>
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPage: Int = 0
    private lateinit var sessionManager : SessionManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homePresenter = HomePresenter(this)
        sessionManager = SessionManager(requireActivity())
        homePresenter.getProduct(page, null, null, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun initListener() {
        homeAdapter = HomeAdapter(requireContext(), arrayListOf())

        gridManager = if (requireActivity().resources.configuration.orientation
            == Configuration.ORIENTATION_PORTRAIT
        ) {
            GridLayoutManager(activity, 2)
        } else {
            GridLayoutManager(activity, 4)
        }
        _binding.rcvProductUser.apply {
            setHasFixedSize(true)
            layoutManager = gridManager
            adapter = homeAdapter
        }
        _binding.swpProductUser.setOnRefreshListener {
            page = 1
            homePresenter.getProduct(page, null, null, null)
        }
        _binding.searchHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null || query != "") {
                    homePresenter.getProduct(null, query, null, null)
                } else {
                    homePresenter.getNextProduct(1)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null || newText != "") {
                    homePresenter.getProduct(null, newText, null, null)
                } else {
                    homePresenter.getNextProduct(page)
                }

                return false
            }

        })
        _binding.rcvProductUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        homeAdapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("data", data[position])
                intent.putParcelableArrayListExtra("image",ArrayList(data[position].product_picture))
                startActivity(intent)
            }
            override fun onButtonClick(position: Int) {
                if (sessionManager.prefIsLogin) {
                    val intent = Intent(requireActivity(), CheckoutActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }

        })
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                _binding.swpProductUser.isRefreshing = true
                _binding.shimmerContainer.visibility = View.VISIBLE
                _binding.shimmerContainer.startShimmer()
                _binding.rcvProductUser.visibility = View.GONE
            }
            false -> {
                _binding.swpProductUser.isRefreshing = false
                _binding.shimmerContainer.stopShimmer()
                _binding.shimmerContainer.visibility = View.GONE
                _binding.rcvProductUser.visibility = View.VISIBLE
            }
        }
    }

    override fun onNextLoading(nextLoading: Boolean) {
        when (nextLoading) {
            true -> {
                isLoading = true
                _binding.progressBarHorizontalHome.visibility = View.VISIBLE
            }
            false -> {
                isLoading = false
                _binding.progressBarHorizontalHome.visibility = View.GONE
            }
        }
    }

    override fun onResultProduct(responseProduct: ResponseProduct) {
        data = responseProduct.data!!
        totalPage = responseProduct.last_page!!
        data.let { homeAdapter.setData(it) }
    }

    override fun onResultNextPage(responseProduct: ResponseProduct) {
        data = responseProduct.data!!
        totalPage = responseProduct.last_page!!
        data.let { homeAdapter.setNextData(it) }
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

}