package com.ekr.jularis.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.response.HistoriData
import com.ekr.jularis.data.response.ResponseHistory
import com.ekr.jularis.databinding.FragmentHomeBinding
import com.ekr.jularis.databinding.FragmentTransactionBinding
import com.ekr.jularis.utils.SessionManager

class TransactionFragment : Fragment(), TransactionContract.View {
    private lateinit var transactionPresenter: TransactionPresenter
    private lateinit var sessionManager: SessionManager
    private lateinit var _binding: FragmentTransactionBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var transactionAdapter: TransactionAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPage: Int = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transactionPresenter = TransactionPresenter(this)
        sessionManager = SessionManager(requireActivity())

    }

    override fun onStart() {
        super.onStart()
        transactionPresenter.getHistoriFull(sessionManager.prefToken, null, null)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun initListener() {
        transactionAdapter = TransactionAdapter(arrayListOf())
        manager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        _binding.rcvHistori.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = transactionAdapter
        }
        _binding.swpHistori.setOnRefreshListener {
            _binding.swpHistori.isRefreshing = false
            page = 1
            transactionPresenter.getHistoriFull(sessionManager.prefToken, page, null)
        }
        _binding.rcvHistori.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val countItem = manager.itemCount
                    val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
                    val isLastPosition = countItem.minus(1) == lastVisiblePosition
                    if (lastVisiblePosition == countItem - 1) {
                        if (!isLoading && isLastPosition && page < totalPage) {
                            page = page.plus(1)
                            transactionPresenter.getHistoriNext(sessionManager.prefToken,page,null)
                        }
                    }
                }
                }

        })
        _binding.searchHistori.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null || query != "") {
                    transactionPresenter.getHistoriFull(sessionManager.prefToken, null, query)
                } else {
                    page = 1
                    transactionPresenter.getHistoriFull(sessionManager.prefToken, page, null)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null || newText != "") {
                    transactionPresenter.getHistoriFull(sessionManager.prefToken, null, newText)
                } else {
                    page = 1
                    transactionPresenter.getHistoriFull(sessionManager.prefToken, page, null)
                }
                return false
            }

        })
        transactionAdapter.setOnItemClickListener(object : TransactionAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, data: HistoriData) {
                Toast.makeText(requireActivity(),position.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun firstLoading(firstLoading: Boolean) {
        when (firstLoading) {
            true -> {
                _binding.rcvHistori.visibility = View.GONE
                _binding.progressBarHorizontalHistori.visibility = View.GONE
                _binding.shimmerHistori.visibility = View.VISIBLE
                _binding.shimmerHistori.startShimmer()
            }
            false -> {
                _binding.progressBarHorizontalHistori.visibility = View.GONE
                _binding.shimmerHistori.stopShimmer()
                _binding.shimmerHistori.visibility = View.GONE
                _binding.rcvHistori.visibility = View.VISIBLE

            }
        }
    }

    override fun nextLoading(nextLoading: Boolean) {
        when (nextLoading) {
            true -> {
                isLoading = true
                _binding.shimmerHistori.visibility = View.GONE
                _binding.progressBarHorizontalHistori.visibility = View.VISIBLE
            }
            false -> {
                isLoading = false
                _binding.templateEmptyTransaction.visibility = View.GONE
                _binding.shimmerHistori.visibility = View.GONE
                _binding.progressBarHorizontalHistori.visibility = View.GONE
            }
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showEmptyCart(message: String) {
        _binding.rcvHistori.visibility = View.GONE
        _binding.progressBarHorizontalHistori.visibility = View.GONE
        _binding.shimmerHistori.visibility = View.GONE
        _binding.templateEmptyTransaction.visibility = View.VISIBLE
        _binding.tvEmptyTrans.text = message
    }

    override fun resultFirstRequest(responseHistory: ResponseHistory) {
        totalPage = responseHistory.last_page
        transactionAdapter.setData(responseHistory.data)
    }

    override fun resultNextRequest(responseHistory: ResponseHistory) {
        totalPage = responseHistory.last_page
        transactionAdapter.setNextData(responseHistory.data)
    }

}