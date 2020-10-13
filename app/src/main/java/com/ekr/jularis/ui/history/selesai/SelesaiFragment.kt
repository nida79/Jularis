package com.ekr.jularis.ui.history.selesai

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.data.histori.HistoriData
import com.ekr.jularis.data.response.ResponseHistory
import com.ekr.jularis.databinding.FragmentSelesaiBinding
import com.ekr.jularis.ui.history.TransactionAdapter
import com.ekr.jularis.ui.history.detail.TransactionActivityDetail
import com.ekr.jularis.utils.SessionManager

class SelesaiFragment : Fragment(), SelesaiContract.View {
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPage: Int = 0
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var selesaiPresenter: SelesaiPresenter
    private lateinit var binding: FragmentSelesaiBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var manager: LinearLayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        selesaiPresenter = SelesaiPresenter(this)
        sessionManager = SessionManager(requireContext())
    }

    override fun onStart() {
        super.onStart()
        page = 1
        selesaiPresenter.getHistoriFirst(sessionManager.prefToken, page, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelesaiBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun initListener() {
        transactionAdapter = TransactionAdapter(arrayListOf())
        manager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rcvSelesai.apply {
            layoutManager = manager
            adapter = transactionAdapter
            setHasFixedSize(true)
        }
        binding.swpSelesai.setOnRefreshListener {
            binding.swpSelesai.isRefreshing = false
            page = 1
            selesaiPresenter.getHistoriFirst(sessionManager.prefToken, page, null)
        }
        binding.rcvSelesai.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val countItem = manager.itemCount
                    val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
                    val isLastPosition = countItem.minus(1) == lastVisiblePosition
                    if (lastVisiblePosition == countItem - 1) {
                        if (!isLoading && isLastPosition && page < totalPage) {
                            page = page.plus(1)
                            selesaiPresenter.getHistoriNext(sessionManager.prefToken, page, null)
                        }
                    }
                }
            }

        })
        binding.searchSelesai.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null || query != "") {
                    selesaiPresenter.getHistoriFirst(sessionManager.prefToken, null, query)
                } else {
                    page = 1
                    selesaiPresenter.getHistoriFirst(sessionManager.prefToken, page, null)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null || newText != "") {
                    selesaiPresenter.getHistoriFirst(sessionManager.prefToken, null, newText)
                } else {
                    page = 1
                    selesaiPresenter.getHistoriFirst(sessionManager.prefToken, page, null)
                }
                return false
            }

        })
        transactionAdapter.setOnItemClickListener(object : TransactionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: HistoriData) {
                val intent = Intent(requireActivity(), TransactionActivityDetail::class.java)
                intent.putExtra("data", data)
                requireActivity().startActivity(intent)
            }
        })
    }

    override fun firstLoading(firstLoading: Boolean) {
        when (firstLoading) {
            true -> {
                binding.rcvSelesai.visibility = View.GONE
                binding.templateEmptySelesai.visibility = View.GONE
                binding.progressBarHorizontalSelesai.visibility = View.GONE
                binding.shimmerSelesai.visibility = View.VISIBLE
                binding.shimmerSelesai.startShimmer()
            }
            false -> {
                binding.templateEmptySelesai.visibility = View.GONE
                binding.progressBarHorizontalSelesai.visibility = View.GONE
                binding.shimmerSelesai.stopShimmer()
                binding.shimmerSelesai.visibility = View.GONE
                binding.rcvSelesai.visibility = View.VISIBLE

            }
        }
    }

    override fun nextLoading(nextLoading: Boolean) {
        when (nextLoading) {
            true -> {
                isLoading = true
                binding.templateEmptySelesai.visibility = View.GONE
                binding.shimmerSelesai.visibility = View.GONE
                binding.progressBarHorizontalSelesai.visibility = View.VISIBLE
            }
            false -> {
                isLoading = false
                binding.templateEmptySelesai.visibility = View.GONE
                binding.shimmerSelesai.visibility = View.GONE
                binding.progressBarHorizontalSelesai.visibility = View.GONE
            }
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showEmptyCart(message: String) {
        binding.rcvSelesai.visibility = View.GONE
        binding.progressBarHorizontalSelesai.visibility = View.GONE
        binding.shimmerSelesai.visibility = View.GONE
        binding.templateEmptySelesai.visibility = View.VISIBLE
        binding.tvEmptyTrans.text = message
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