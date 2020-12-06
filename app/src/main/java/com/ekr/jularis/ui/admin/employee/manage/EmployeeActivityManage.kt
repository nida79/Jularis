package com.ekr.jularis.ui.admin.employee.manage

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ekr.jularis.R
import com.ekr.jularis.data.employee.EmployeeAdd
import com.ekr.jularis.data.employee.EmployeeData
import com.ekr.jularis.data.response.ResponseGlobal
import com.ekr.jularis.ui.admin.employee.EmployeeKegiatanAdapter
import com.ekr.jularis.utils.DialogHelper
import com.ekr.jularis.utils.SessionManager
import kotlinx.android.synthetic.main.activity_employee_manage.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*

class EmployeeActivityManage : AppCompatActivity(), EmployeeManageContract.View {
    private var employeeData: EmployeeData? = null
    private lateinit var employeeKegiatanAdapter: EmployeeKegiatanAdapter
    private lateinit var presenter: EmployeeManagePresenter
    private lateinit var dialog: Dialog
    private lateinit var sessionManager: SessionManager
    private lateinit var employeeAdd: EmployeeAdd
    private lateinit var confirm: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_manage)
        dialog = DialogHelper.globalLoading(this)
        presenter = EmployeeManagePresenter(this)
        sessionManager = SessionManager(this)
        confirm = DialogHelper.bottomSheetDialogKonfirm(this)
    }

    override fun initListener() {
        employeeData = intent.getParcelableExtra("data")
        employeeKegiatanAdapter = EmployeeKegiatanAdapter(arrayListOf())
        if (employeeData != null) {
            tv_header_karyawan.text = "Update Data Karyawan"
            img_delete_karyawan.visibility = View.VISIBLE
            tie_krw_email.setText(employeeData!!.email)
            tie_krw_username.setText(employeeData!!.username)
            wadah_krw_email.visibility = View.GONE
            wadah_krw_username.visibility = View.GONE
            wadah_krw_pswd.visibility = View.GONE
            tie_krw_nama.setText(employeeData!!.name)
            tie_krw_alamat.setText(employeeData!!.address)
            tie_krw_nohp.setText(employeeData!!.noTelp)
            btn_krw_submit.text = "Update"
            btn_krw_submit.setOnClickListener {
                when {
                    tie_krw_nama.text.toString().isEmpty() -> {
                        tie_krw_nama.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_nama.requestFocus()
                    }

                    tie_krw_alamat.text.toString().isEmpty() -> {
                        tie_krw_alamat.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_alamat.requestFocus()
                    }
                    tie_krw_nohp.text.toString().isEmpty() -> {
                        tie_krw_nohp.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_nohp.requestFocus()
                    }
                    else -> {
                        confirm.text_keterangan.text = "Update Data Karyawan ?"
                        confirm.btn_sheet_cancel.text = "Iya"
                        confirm.btn_sheet_ok.text = "Tidak"
                        confirm.show()
                        employeeAdd = EmployeeAdd(
                            tie_krw_username.text.toString(),
                            tie_krw_nama.text.toString(),
                            tie_krw_email.text.toString(),
                            null,
                            tie_krw_nohp.text.toString(),
                            tie_krw_alamat.text.toString()
                        )
                        confirm.btn_sheet_cancel.setOnClickListener {
                            presenter.doUpdateKaryawan(
                                sessionManager.prefToken,
                                employeeData!!.id.toString(), employeeAdd
                            )
                            Log.e("update", employeeAdd.address + employeeAdd.email +employeeAdd.noTelp )
                            confirm.dismiss()
                        }
                        confirm.btn_sheet_ok.setOnClickListener {
                            confirm.dismiss()
                        }
                    }
                }
            }
            img_delete_karyawan.setOnClickListener {
                confirm.text_keterangan.text = "Hapus Data Karyawan ?"
                confirm.btn_sheet_cancel.text = "Tidak"
                confirm.btn_sheet_ok.text = "Iya"
                confirm.show()
                confirm.btn_sheet_cancel.setOnClickListener {
                    confirm.dismiss()
                }
                confirm.btn_sheet_ok.setOnClickListener {
                    presenter.doDeleteKaryawan(sessionManager.prefToken, employeeData!!.id.toString())
                    confirm.dismiss()
                }
            }
        }
        else {
            btn_krw_submit.setOnClickListener {
                when {
                    tie_krw_username.text.toString().isEmpty() -> {
                        tie_krw_username.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_username.requestFocus()
                    }
                    tie_krw_email.text.toString().isEmpty() -> {
                        tie_krw_email.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_email.requestFocus()
                    }
                    tie_krw_nama.text.toString().isEmpty() -> {
                        tie_krw_nama.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_nama.requestFocus()
                    }
                    tie_krw_alamat.text.toString().isEmpty() -> {
                        tie_krw_alamat.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_alamat.requestFocus()
                    }
                    tie_krw_nohp.text.toString().isEmpty() -> {
                        tie_krw_nohp.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_nohp.requestFocus()
                    }
                    tie_krw_pswd.text.toString().isEmpty() -> {
                        tie_krw_pswd.error = "Kolom Tidak Boleh Kosong"
                        tie_krw_pswd.requestFocus()
                    }
                    else -> {
                        confirm.text_keterangan.text = "Tambah Data Karyawan ?"
                        confirm.btn_sheet_cancel.text = "Iya"
                        confirm.btn_sheet_ok.text = "Tidak"
                        confirm.show()
                        employeeAdd = EmployeeAdd(
                            tie_krw_username.text.toString(),
                            tie_krw_nama.text.toString(),
                            tie_krw_email.text.toString(),
                            tie_krw_pswd.text.toString(),
                            tie_krw_nohp.text.toString(),
                            tie_krw_alamat.text.toString()
                        )
                        confirm.btn_sheet_cancel.setOnClickListener {
                            presenter.doAddKaryawan(
                                sessionManager.prefToken, employeeAdd
                            )
                            confirm.dismiss()
                        }
                        confirm.btn_sheet_ok.setOnClickListener {
                            confirm.dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun loading(loading: Boolean) {
       when(loading){
           true->dialog.show()
           false->dialog.dismiss()
       }
    }

    override fun resultManage(responseGlobal: ResponseGlobal) {
       if (responseGlobal.status){
           finish()
       }
    }

    override fun showMessage(message: String) {
       Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}