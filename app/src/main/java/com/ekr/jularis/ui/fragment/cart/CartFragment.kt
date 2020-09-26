package com.ekr.jularis.ui.fragment.cart

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.cart.Checkout
import com.ekr.jularis.data.response.ResponseCart
import com.ekr.jularis.databinding.FragmentCartBinding
import com.ekr.jularis.utils.SessionManager

class CartFragment : Fragment(), CartContract.View {
    private lateinit var cartPresenter: CartPresenter
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var checkout: List<Checkout>
    private lateinit var binding:FragmentCartBinding

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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initListener() {
        cartAdapter = CartAdapter(requireContext(), arrayListOf())
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
        binding.cbAll.setOnCheckedChangeListener { _, b ->
            when (b) {
                true -> {
                    binding.tvDelete.setTextColor(Color.parseColor("#E91E63"))
                    binding.tvDelete.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        resources.getDrawable(
                            R.drawable.ic_delete_red
                        ), null, null, null
                    )
                    binding.cbAll.setTextColor(Color.parseColor("#67C2CB"))
                }
                false -> {
                    binding.tvDelete.setTextColor(Color.parseColor("#7D7D7E"))
                    binding.tvDelete.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        resources.getDrawable(
                            R.drawable.ic_delete_deftault
                        ), null, null, null
                    )
                    binding.cbAll.setTextColor(Color.parseColor("#7D7D7E"))
                }
            }
        }
    }

    override fun showMessage(message: String) {
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

    override fun onResult(responseCart: ResponseCart) {
        checkout = responseCart.checkout!!
        checkout.let { cartAdapter.setData(checkout) }
    }

}