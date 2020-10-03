package com.ekr.jularis.ui.cart

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.cart.Checkout
import com.ekr.jularis.data.response.ResponseCart
import com.ekr.jularis.databinding.FragmentCartBinding
import com.ekr.jularis.utils.MoneyHelper
import com.ekr.jularis.utils.SessionManager
import es.dmoral.toasty.Toasty

class CartFragment : Fragment(), CartContract.View {
    private lateinit var cartPresenter: CartPresenter
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var checkout: List<Checkout>
    private lateinit var binding: FragmentCartBinding
    private  var checked:Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cartPresenter = CartPresenter(this)
        sessionManager = SessionManager(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        cartPresenter.getCartlist(sessionManager.prefToken)
    }

    override fun initListener() {
        cartAdapter = CartAdapter(requireContext(), arrayListOf(),false)
        binding.rcvDaftarKeranjang.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        binding.swpCartUser.setOnRefreshListener {
            binding.templateFilledCart.visibility = View.VISIBLE
            binding.templateEmptyCart.visibility = View.GONE
            cartPresenter.getCartlist(sessionManager.prefToken)
        }
        cartAdapter.setOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onButtonPlusClick(checkout: Checkout) {
                cartPresenter.doCalculatePlus(
                    sessionManager.prefToken,
                    checkout.checked,
                    checkout.productId,
                    checkout.quantity
                )
            }

            override fun onButtonMinusClick(checkout: Checkout) {
                cartPresenter.doCalculateMinus(
                    sessionManager.prefToken,
                    checkout.checked,
                    checkout.productId,
                    checkout.quantity
                )
            }

            override fun onCheckBoxClick(checkout: Checkout) {
                cartPresenter.doCheckBoxClicked(
                    sessionManager.prefToken,
                    checkout.checked,
                    checkout.productId,
                    checkout.quantity
                )
            }

        })
        binding.tvDelete.setOnClickListener {
            cartPresenter.doDeleteItem(sessionManager.prefToken)
        }
        actionCheckAll()

    }

    override fun showEmptyCart(message: String) {
        binding.tvEmptyCart.text = message
        binding.templateFilledCart.visibility = View.GONE
        binding.templateEmptyCart.visibility = View.VISIBLE
    }

    override fun onLoading(boolean: Boolean) {
        when (boolean) {
            true -> {
                binding.swpCartUser.isRefreshing = true
                binding.shimmerCartContainer.visibility = View.VISIBLE
                binding.shimmerCartContainer.startShimmer()
                binding.rcvDaftarKeranjang.visibility = View.GONE
            }
            false -> {
                binding.swpCartUser.isRefreshing = false
                binding.shimmerCartContainer.stopShimmer()
                binding.shimmerCartContainer.visibility = View.GONE
                binding.rcvDaftarKeranjang.visibility = View.VISIBLE
            }
        }
    }

    override fun showToast(message: String) {
        Toasty.info(requireActivity(), message, Toasty.LENGTH_SHORT).show()
    }

    override fun loadingHorizontal(boolean: Boolean) {
        when (boolean) {
            true -> binding.progressBarHorizontalCart.visibility = View.VISIBLE
            false -> binding.progressBarHorizontalCart.visibility = View.GONE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun actionCheckAll() {
        binding.cbAll.setOnCheckedChangeListener { _, b ->
            if (!b) {
                cartPresenter.doCheckAll(sessionManager.prefToken,checked)
                binding.cbAll.setTextColor(Color.parseColor("#7D7D7E"))

            } else {
                cartPresenter.doCheckAll(sessionManager.prefToken,checked)
                binding.cbAll.setTextColor(Color.parseColor("#67C2CB"))
            }
        }
    }

    override fun resultUpdate(message: String) {
        cartPresenter.getCartUpdate(sessionManager.prefToken)
    }

    override fun onResult(responseCart: ResponseCart) {
        checkout = responseCart.checkout!!
        checkout.let { cartAdapter.setData(checkout) }
        val uang = responseCart.total_amount
        if (uang == 0) {
            checked = 1
            binding.btnKeranjangBuy.visibility = View.GONE
            binding.tvDelete.isEnabled = false
            binding.tvDelete.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.abu_soft
                )
            )
            binding.tvDelete.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_delete_deftault
                ), null, null, null
            )

        } else {
            checked = 0
            binding.btnKeranjangBuy.visibility = View.VISIBLE
            binding.tvDelete.isEnabled = true
            binding.tvDelete.setTextColor(ContextCompat.getColor(requireContext(), R.color.pink))
            binding.tvDelete.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_delete_red
                ), null, null, null
            )
        }
        responseCart.total_amount?.let { MoneyHelper.setRupiah(binding.totalKeranjang, it) }
    }
}