package com.ekr.jularis.ui.admin.employee.list

import android.app.Dialog
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
import com.ekr.jularis.data.employee.EmployeeData
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.response.ResponseGetEmployee
import com.ekr.jularis.databinding.FragmentEmployeeListBinding
import com.ekr.jularis.databinding.FragmentProductBinding
import com.ekr.jularis.ui.admin.employee.EmployeeAdapter
import com.ekr.jularis.ui.admin.employee.manage.EmployeeActivityManage
import com.ekr.jularis.ui.admin.product.ProductAdapter
import com.ekr.jularis.ui.admin.product.mange.ProductActivityManage
import com.ekr.jularis.utils.SessionManager

class EmployeeFragmentList : Fragment(), ListEmployeeContract.View {
    private lateinit var bindingFragmentList: FragmentEmployeeListBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var employeePresenter: ListEmployeePresenter
    private var page = 1
    private var totalPage = 0
    private lateinit var employeeAdapter: EmployeeAdapter
    private lateinit var manager: LinearLayoutManager
    private var isLoading: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        employeePresenter = ListEmployeePresenter(this)
        sessionManager = SessionManager(requireContext())
    }

    override fun onStart() {
        super.onStart()
        page = 1
        employeePresenter.getEmployee(sessionManager.prefToken, page, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFragmentList = FragmentEmployeeListBinding.inflate(layoutInflater)
        return bindingFragmentList.root
    }

    override fun initListener() {
        employeeAdapter = EmployeeAdapter(arrayListOf())
        manager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        bindingFragmentList.rcvEmployee.apply {
            layoutManager = manager
            setHasFixedSize(true)
            adapter = employeeAdapter

        }
        bindingFragmentList.swpEmployee.setOnRefreshListener {
            bindingFragmentList.swpEmployee.isRefreshing = false
            page = 1
            employeePresenter.getEmployee(sessionManager.prefToken, page, null)
        }
        bindingFragmentList.rcvEmployee.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val countItem = manager.itemCount
                    val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
                    val isLastPosition = countItem.minus(1) == lastVisiblePosition
                    if (lastVisiblePosition == countItem - 1) {
                        if (!isLoading && isLastPosition && page < totalPage) {
                            page = page.plus(1)
                            employeePresenter.getNextPage(sessionManager.prefToken, page)
                        }
                    }
                }
            }

        })
        bindingFragmentList.searchEmployee.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null || query != "") {
                    employeePresenter.getEmployee(sessionManager.prefToken, null, query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null || newText != "") {
                    employeePresenter.getEmployee(sessionManager.prefToken, null, newText)
                }
                return true
            }

        })
        bindingFragmentList.fabAddEmployee.setOnClickListener {
            startActivity(Intent(requireActivity(), EmployeeActivityManage::class.java))
        }
        employeeAdapter.setOnItemClickListener(object : EmployeeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: EmployeeData) {
                val intent = Intent(requireContext(), EmployeeActivityManage::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
            }
        })
    }

    override fun firstLoading(loading: Boolean) {
        when (loading) {
            true -> {
                bindingFragmentList.swpEmployee.isRefreshing = true
                bindingFragmentList.shimmerContainerEmploye.visibility = View.VISIBLE
                bindingFragmentList.shimmerContainerEmploye.startShimmer()
                bindingFragmentList.rcvEmployee.visibility = View.GONE
            }
            false -> {
                bindingFragmentList.swpEmployee.isRefreshing = false
                bindingFragmentList.shimmerContainerEmploye.stopShimmer()
                bindingFragmentList.shimmerContainerEmploye.visibility = View.GONE
                bindingFragmentList.rcvEmployee.visibility = View.VISIBLE
            }
        }
    }

    override fun nextLoading(nextLoading: Boolean) {
        when (nextLoading) {
            true -> {
                isLoading = true
                bindingFragmentList.progressBarHorizontalEmployee.visibility = View.VISIBLE
            }
            false -> {
                isLoading = false
                bindingFragmentList.progressBarHorizontalEmployee.visibility = View.GONE
            }
        }
    }

    override fun resultGetEmployee(responseGetEmployee: ResponseGetEmployee) {
        employeeAdapter.setData(responseGetEmployee.data!!)
        totalPage = responseGetEmployee.total_page
    }

    override fun resultNextPage(responseGetEmployee: ResponseGetEmployee) {
        employeeAdapter.setNextData(responseGetEmployee.data!!)
        totalPage = responseGetEmployee.total_page
    }

    override fun showMessage(message: String) {
        if (context != null) {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }

}