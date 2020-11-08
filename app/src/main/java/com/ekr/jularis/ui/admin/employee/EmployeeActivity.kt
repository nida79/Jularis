package com.ekr.jularis.ui.admin.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ekr.jularis.R
import com.ekr.jularis.ui.admin.employee.aktifitas.EmployeeFragmentAktifitas
import com.ekr.jularis.ui.admin.employee.list.EmployeeFragmentList
import com.ekr.jularis.ui.admin.employee.manage.EmployeeActivityManage
import com.ekr.jularis.ui.history.ViewPagerAdapter
import com.ekr.jularis.ui.history.prosess.ProsesFragment
import com.ekr.jularis.ui.history.selesai.SelesaiFragment
import kotlinx.android.synthetic.main.activity_employee.*

class EmployeeActivity : AppCompatActivity() {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(EmployeeFragmentList(), "Daftar Karyawan")
        viewPagerAdapter.addFragment(EmployeeFragmentAktifitas(), "Kegiatan")
        vp_employee.adapter = viewPagerAdapter
        tab_layout_employee.setupWithViewPager(vp_employee)
    }
}