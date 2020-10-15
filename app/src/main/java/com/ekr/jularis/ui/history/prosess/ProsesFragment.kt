package com.ekr.jularis.ui.history.prosess

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
import com.ekr.jularis.data.response.HistoriNewData
import com.ekr.jularis.data.response.ResponseHistory
import com.ekr.jularis.data.response.ResponseNewHistori
import com.ekr.jularis.databinding.FragmentProsesBinding
import com.ekr.jularis.ui.history.TransactionAdapter
import com.ekr.jularis.ui.history.detail.TransactionActivityDetail
import com.ekr.jularis.utils.SessionManager

class ProsesFragment : Fragment(), ProsesContract.View {
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPage: Int = 0
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var prosesBinding: FragmentProsesBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var manager: LinearLayoutManager
    private lateinit var prosesPresenter: ProsesPresenter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prosesPresenter = ProsesPresenter(this)
        sessionManager = SessionManager(requireContext())

    }

    override fun onStart() {
        super.onStart()
        page = 1
        prosesPresenter.getHistoriFirst(sessionManager.prefToken, page, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prosesBinding = FragmentProsesBinding.inflate(layoutInflater)
        return prosesBinding.root
    }

    override fun initListener() {
        transactionAdapter = TransactionAdapter(arrayListOf())
        manager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        prosesBinding.rcvProses.apply {
            layoutManager = manager
            adapter = transactionAdapter
            setHasFixedSize(true)
        }
        prosesBinding.swpProses.setOnRefreshListener {
            prosesBinding.swpProses.isRefreshing = false
            page = 1
            prosesPresenter.getHistoriFirst(sessionManager.prefToken, page, null)
        }
        prosesBinding.rcvProses.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val countItem = manager.itemCount
                    val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
                    val isLastPosition = countItem.minus(1) == lastVisiblePosition
                    if (lastVisiblePosition == countItem - 1) {
                        if (!isLoading && isLastPosition && page < totalPage) {
                            page = page.plus(1)
                            prosesPresenter.getHistoriNext(sessionManager.prefToken, page, null)
                        }
                    }
                }
            }

        })
        prosesBinding.searchProses.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null || query != "") {
                    prosesPresenter.getHistoriFirst(sessionManager.prefToken, null, query)
                } else {
                    page = 1
                    prosesPresenter.getHistoriFirst(sessionManager.prefToken, page, null)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null || newText != "") {
                    prosesPresenter.getHistoriFirst(sessionManager.prefToken, null, newText)
                } else {
                    page = 1
                    prosesPresenter.getHistoriFirst(sessionManager.prefToken, page, null)
                }
                return false
            }

        })
        transactionAdapter.setOnItemClickListener(object : TransactionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: HistoriNewData) {
                val intent = Intent(requireActivity(), TransactionActivityDetail::class.java)
                intent.putExtra("data", data)
                requireActivity().startActivity(intent)
            }
        })
    }

    override fun firstLoading(firstLoading: Boolean) {
        when (firstLoading) {
            true -> {
                prosesBinding.rcvProses.visibility = View.GONE
                prosesBinding.templateEmptyProses.visibility = View.GONE
                prosesBinding.progressBarHorizontalProses.visibility = View.GONE
                prosesBinding.shimmerProses.visibility = View.VISIBLE
                prosesBinding.shimmerProses.startShimmer()
            }
            false -> {
                prosesBinding.templateEmptyProses.visibility = View.GONE
                prosesBinding.progressBarHorizontalProses.visibility = View.GONE
                prosesBinding.shimmerProses.stopShimmer()
                prosesBinding.shimmerProses.visibility = View.GONE
                prosesBinding.rcvProses.visibility = View.VISIBLE

            }
        }
    }

    override fun nextLoading(nextLoading: Boolean) {
        when (nextLoading) {
            true -> {
                isLoading = true
                prosesBinding.templateEmptyProses.visibility = View.GONE
                prosesBinding.shimmerProses.visibility = View.GONE
                prosesBinding.progressBarHorizontalProses.visibility = View.VISIBLE
            }
            false -> {
                isLoading = false
                prosesBinding.templateEmptyProses.visibility = View.GONE
                prosesBinding.shimmerProses.visibility = View.GONE
                prosesBinding.progressBarHorizontalProses.visibility = View.GONE
            }
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showEmptyCart(message: String) {
        prosesBinding.rcvProses.visibility = View.GONE
        prosesBinding.progressBarHorizontalProses.visibility = View.GONE
        prosesBinding.shimmerProses.visibility = View.GONE
        prosesBinding.templateEmptyProses.visibility = View.VISIBLE
        prosesBinding.tvEmptyTrans.text = message
    }

    override fun resultFirstRequest(responseHistory: ResponseNewHistori) {
        totalPage = responseHistory.lastPage
        transactionAdapter.setData(responseHistory.data)
    }

    override fun resultNextRequest(responseHistory: ResponseNewHistori) {
        totalPage = responseHistory.lastPage
        transactionAdapter.setNextData(responseHistory.data)
    }
}