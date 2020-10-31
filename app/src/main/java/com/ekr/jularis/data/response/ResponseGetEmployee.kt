package com.ekr.jularis.data.response


import com.ekr.jularis.data.employee.EmployeeData
import com.google.gson.annotations.SerializedName

data class ResponseGetEmployee(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("data") val data: List<EmployeeData>?,
    @SerializedName("total") val total_page: Int
)