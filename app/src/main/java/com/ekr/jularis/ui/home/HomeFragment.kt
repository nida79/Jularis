package com.ekr.jularis.ui.home

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekr.jularis.R
import com.ekr.jularis.data.cart.postcheckout.Data
import com.ekr.jularis.data.product.DataProduct
import com.ekr.jularis.data.response.ResponseProduct
import com.ekr.jularis.databinding.FragmentHomeBinding
import com.ekr.jularis.ui.detail.DetailActivity
import com.ekr.jularis.ui.login.LoginActivity
import com.ekr.jularis.ui.payment.PaymentActivity
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.SessionManager
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.dialog_count_product.*


class HomeFragment : Fragment(), HomeContract.View {
    private lateinit var _binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var gridManager: GridLayoutManager
    private lateinit var homePresenter: HomePresenter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private lateinit var dialog: Dialog
    private var totalPage: Int = 0
    private lateinit var sessionManager: SessionManager
    private lateinit var dialogFCM: Dialog
    private var qty = 1
    private var firebasetoken = ""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialogFCM = DialogHelper.globalLoading(requireActivity())
        homePresenter = HomePresenter(this)
        sessionManager = SessionManager(requireActivity())
        dialog = Dialog(requireActivity())
    }

    override fun onStart() {
        super.onStart()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task: Task<InstanceIdResult> ->
                if (task.isSuccessful && task.isComplete) {
                    firebasetoken = task.result!!.token
                    Log.e("inidia", "tokenbaru: $firebasetoken")
                    if (sessionManager.prefIsLogin) {
                        if (sessionManager.prefFcm != firebasetoken) {
                            sessionManager.prefFcm=firebasetoken
                            homePresenter.doUpdateFcm(sessionManager.prefToken, firebasetoken)
                        }
                    }
                }
            }

        homePresenter.getProduct(page, null, null, null)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onPause() {
        super.onPause()
        page = 1
    }

    override fun onResume() {
        super.onResume()
        page = 1
        homePresenter.getProduct(page, null, null, null)
    }

    override fun initListener() {
        homeAdapter = HomeAdapter(arrayListOf())
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
                    page = 1
                    homePresenter.getProduct(page, null, null, null)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null || newText != "") {
                    homePresenter.getProduct(null, newText, null, null)
                } else {
                    page = 1
                    homePresenter.getProduct(page, null, null, null)
                }
                return false
            }

        })
        _binding.rcvProductUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val countItem = gridManager.itemCount
                    val lastVisiblePosition = gridManager.findLastCompletelyVisibleItemPosition()
                    if (lastVisiblePosition == countItem - 1) {
                        if (!isLoading && page < totalPage) {
                            page = page.plus(1)
                            homePresenter.getNextProduct(page)
                        }
                    }
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

    override fun fcmLoading(fcmLoading: Boolean) {
        when (fcmLoading) {
            true -> dialogFCM.show()
            false -> dialogFCM.dismiss()
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
        responseProduct.data?.let { homeAdapter.insertItem(it) }
        totalPage = responseProduct.last_page!!

    }

    override fun onResultNextPage(responseProduct: ResponseProduct) {
        responseProduct.data?.let { homeAdapter.updateItem(it) }
        totalPage = responseProduct.last_page!!

    }

    override fun showMessage(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun resultCounter(int: Int) {
        qty = int
        dialog.pop_up_tv_result.text = qty.toString()
        if (qty <= 1) {
            dialog.pop_up_min.visibility = View.GONE
        } else {
            dialog.pop_up_min.visibility = View.VISIBLE
        }
    }

    override fun resultBuy(message: String, data: Data) {
        dialog.dismiss()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), PaymentActivity::class.java)
        intent.putExtra("pd_id", data.productId)
        startActivity(intent)
    }

    override fun actionAdapterClick() {
        homeAdapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: DataProduct) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("data", data)
                intent.putParcelableArrayListExtra("image", ArrayList(data.product_picture))
                startActivity(intent)
            }

            override fun onButtonClick(position: Int, data: DataProduct) {
                if (sessionManager.prefIsLogin) {
                    dialog.setContentView(R.layout.dialog_count_product)
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.window!!.setLayout(
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager
                            .LayoutParams.WRAP_CONTENT
                    )
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
                    dialog.setCancelable(true)
                    dialog.show()
                    dialog.pop_up_pls.setOnClickListener {
                        homePresenter.doCalculatePlus(qty)
                    }
                    dialog.pop_up_min.setOnClickListener {
                        homePresenter.doCalculateMinus(qty)
                    }
                    dialog.pop_up_cancel_detail.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.pop_up_submit.setOnClickListener {
                        homePresenter.doBuy(
                            sessionManager.prefToken, data.product_id, qty
                        )
                        dialog.dismiss()
                    }
                } else {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }

}