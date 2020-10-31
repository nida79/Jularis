package com.ekr.jularis.ui.admin.employee.aktifitas

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
import com.ekr.jularis.R
import com.ekr.jularis.data.response.ResponseKegiatanEmployee
import com.ekr.jularis.databinding.FragmentEmployeeAktifitasBinding
import com.ekr.jularis.databinding.FragmentEmployeeListBinding
import com.ekr.jularis.ui.admin.employee.EmployeeKegiatanAdapter
import com.ekr.jularis.ui.admin.employee.manage.EmployeeActivityManage
import com.ekr.jularis.utils.SessionManager

class EmployeeFragmentAktifitas : Fragment(),EmployeeKegiatanContract.View {
    private lateinit var binding: FragmentEmployeeAktifitasBinding
    private lateinit var employeeKegiatanPresenter: EmployeeKegiatanPresenter
    private lateinit var employeeKegiatanAdapter: EmployeeKegiatanAdapter
    private lateinit var sessionManager: SessionManager
    private var page = 1
    private var totalPage = 0
    private var isLoading: Boolean = false
    private lateinit var manager: LinearLayoutManager
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        employeeKegiatanPresenter = EmployeeKegiatanPresenter(this)
        sessionManager = SessionManager(requireContext())

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeeAktifitasBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        page = 1
        employeeKegiatanPresenter.getKegiatanEmployee(sessionManager.prefToken,page,null)
    }

    override fun initListener() {
        employeeKegiatanAdapter = EmployeeKegiatanAdapter(arrayListOf())
        manager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rcvKegiatanEmployee.apply {
            layoutManager = manager
            setHasFixedSize(true)
            adapter = employeeKegiatanAdapter
        }
        binding.swpKegiatanEmployee.setOnRefreshListener {
            binding.swpKegiatanEmployee.isRefreshing = false
            page = 1
            employeeKegiatanPresenter.getKegiatanEmployee(sessionManager.prefToken, page, null)
        }
        binding.rcvKegiatanEmployee.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val countItem = manager.itemCount
                    val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
                    val isLastPosition = countItem.minus(1) == lastVisiblePosition
                    if (lastVisiblePosition == countItem - 1) {
                        if (!isLoading && isLastPosition && page < totalPage) {
                            page = page.plus(1)
                            employeeKegiatanPresenter.getNextPage(sessionManager.prefToken, page)
                        }
                    }
                }
            }

        })
        binding.searchKegiatanEmployee.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null || query != "") {
                    employeeKegiatanPresenter.getKegiatanEmployee(sessionManager.prefToken, null, query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null || newText != "") {
                    employeeKegiatanPresenter.getKegiatanEmployee(sessionManager.prefToken, null, newText)
                }
                return true
            }

        })

    }

    override fun firstLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.templateEmptyKegiatan.visibility = View.GONE
                binding.swpKegiatanEmployee.isRefreshing = true
                binding.shimmerContainerKegiatanEmploye.visibility = View.VISIBLE
                binding.shimmerContainerKegiatanEmploye.startShimmer()
                binding.rcvKegiatanEmployee.visibility = View.GONE
            }
            false -> {
                binding.templateEmptyKegiatan.visibility = View.GONE
                binding.swpKegiatanEmployee.isRefreshing = false
                binding.shimmerContainerKegiatanEmploye.stopShimmer()
                binding.shimmerContainerKegiatanEmploye.visibility = View.GONE
                binding.rcvKegiatanEmployee.visibility = View.VISIBLE
            }
        }
    }

    override fun nextLoading(nextLoading: Boolean) {
        when (nextLoading) {
            true -> {
                isLoading = true
                binding.progressBarHorizontalKegiatanEmployee.visibility = View.VISIBLE
            }
            false -> {
                isLoading = false
                binding.progressBarHorizontalKegiatanEmployee.visibility = View.GONE
            }
        }
    }

    override fun resultKegiatanEmployee(responseKegiatanEmployee: ResponseKegiatanEmployee) {
        employeeKegiatanAdapter.setData(responseKegiatanEmployee.data)
        totalPage = responseKegiatanEmployee.total
        if (responseKegiatanEmployee.data.toString() == "[]"){
            binding.templateEmptyKegiatan.visibility = View.VISIBLE
            binding.rcvKegiatanEmployee.visibility = View.GONE
        }
    }

    override fun resultNextPage(responseKegiatanEmployee: ResponseKegiatanEmployee) {
       employeeKegiatanAdapter.setNextData(responseKegiatanEmployee.data)
        totalPage = responseKegiatanEmployee.total

    }

    override fun showMessage(message: String) {
       Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

}