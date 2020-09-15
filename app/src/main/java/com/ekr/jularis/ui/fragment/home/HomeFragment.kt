package com.ekr.jularis.ui.fragment.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ekr.jularis.R
import com.ekr.jularis.data.ExampleItem
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_home_user.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val exampleList = generateDummyList(10)
        sessionManager = SessionManager(requireActivity())
        tvLocation.text = sessionManager.prefFullname
        rcv_product_user.apply {
            adapter = activity?.let { HomeAdapter(it, exampleList) }
            layoutManager =
                if (requireActivity().resources.configuration.orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                    GridLayoutManager(activity, 2)
                }
                else {
                    GridLayoutManager(activity, 4)
                }
            setHasFixedSize(true)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun generateDummyList(size: Int): List<ExampleItem> {
        val list = ArrayList<ExampleItem>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.test
                1 -> R.drawable.test
                else -> R.drawable.test
            }
            val item = ExampleItem(drawable, "Item $i", "Rp.10.000")
            list += item
        }
        return list
    }

}