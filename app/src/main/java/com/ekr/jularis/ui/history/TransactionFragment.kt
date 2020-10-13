package com.ekr.jularis.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekr.jularis.databinding.FragmentTransactionBinding
import com.ekr.jularis.ui.history.prosess.ProsesFragment
import com.ekr.jularis.ui.history.selesai.SelesaiFragment

class TransactionFragment : Fragment() {
    private lateinit var _binding: FragmentTransactionBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(SelesaiFragment(), "Selesai")
        viewPagerAdapter.addFragment(ProsesFragment(), "Proses")
        _binding.vpHistori.adapter = viewPagerAdapter
        _binding.tabLayoutHistori.setupWithViewPager(_binding.vpHistori)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionBinding.inflate(layoutInflater)
        return _binding.root
    }
    companion object {

        @JvmStatic
        fun newInstance() = TransactionFragment()
    }
}