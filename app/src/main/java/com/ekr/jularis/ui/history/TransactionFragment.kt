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
import com.ekr.jularis.ui.history.prosess.ProsesFragment
import com.ekr.jularis.ui.history.selesai.SelesaiFragment
import com.ekr.jularis.utils.SessionManager

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


}