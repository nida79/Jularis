package com.ekr.jularis.ui.admin.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.response.ResponseProduct
import com.ekr.jularis.databinding.FragmentProductBinding
import com.ekr.jularis.ui.admin.product.mange.ProductActivityManage
import com.ekr.jularis.utils.SessionManager

class ProductFragment : Fragment(), ProductContract.View {
    private lateinit var productPresenter: ProductPresenter
    private lateinit var sessionManager: SessionManager
    private var isLoading: Boolean = false
    private var page: Int = 1
    private lateinit var manager: LinearLayoutManager
    private var totalPage: Int = 0
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: FragmentProductBinding
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productPresenter = ProductPresenter(this,requireContext())
        sessionManager = SessionManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        page = 1
        productPresenter.getProduct(page, null, null, null)
    }

    override fun initListener() {
        productAdapter = ProductAdapter(arrayListOf())
        manager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rcvProductAdmin.apply {
            layoutManager = manager
            adapter = productAdapter
            setHasFixedSize(true)
        }
        binding.swpProductAdmin.setOnRefreshListener {
            binding.swpProductAdmin.isRefreshing = false
            page = 1
            productPresenter.getProduct(page, null, null, null)
        }
        binding.rcvProductAdmin.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val countItem = manager.itemCount
                    val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
                    val isLastPosition = countItem.minus(1) == lastVisiblePosition
                    if (lastVisiblePosition == countItem - 1) {
                        if (!isLoading && isLastPosition && page < totalPage) {
                            page = page.plus(1)
                            productPresenter.getNextProduct(page)
                        }
                    }
                }
            }

        })
        binding.searchAdminProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null || query != "") {
                    productPresenter.getProduct(null, query, null, null)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null || newText != "") {
                    productPresenter.getProduct(null, newText, null, null)
                }
                return true
            }

        })
        binding.fabAddProduct.setOnClickListener {
            startActivity(Intent(requireActivity(),ProductActivityManage::class.java))
        }
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.swpProductAdmin.isRefreshing = true
                binding.shimmerContainerAdmin.visibility = View.VISIBLE
                binding.shimmerContainerAdmin.startShimmer()
                binding.rcvProductAdmin.visibility = View.GONE
            }
            false -> {
                binding.swpProductAdmin.isRefreshing = false
                binding.shimmerContainerAdmin.stopShimmer()
                binding.shimmerContainerAdmin.visibility = View.GONE
                binding.rcvProductAdmin.visibility = View.VISIBLE
            }
        }
    }

    override fun onNextLoading(nextLoading: Boolean) {
        when (nextLoading) {
            true -> {
                isLoading = true
                binding.progressBarHorizontalAdminProduct.visibility = View.VISIBLE
            }
            false -> {
                isLoading = false
                binding.progressBarHorizontalAdminProduct.visibility = View.GONE
            }
        }
    }

    override fun onResultProduct(responseProduct: ResponseProduct) {
        productAdapter.setData(responseProduct.data!!)
        totalPage = responseProduct.last_page!!

    }

    override fun onResultNextPage(responseProduct: ResponseProduct) {
        productAdapter.setNextData(responseProduct.data!!)
        totalPage = responseProduct.last_page!!

    }

    override fun showMessage(message: String) {
        if (context!=null){
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun actionAdapterClick() {
        productAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: DataProduct) {
                val intent = Intent(requireContext(), ProductActivityManage::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
            }

        })
    }

}