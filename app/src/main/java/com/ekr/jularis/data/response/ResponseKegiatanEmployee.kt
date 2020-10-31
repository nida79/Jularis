package com.ekr.jularis.data.response


import com.ekr.jularis.data.employee.KegiatanData
import com.google.gson.annotations.SerializedName

data class ResponseKegiatanEmployee(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("data") val data: List<KegiatanData>,
    @SerializedName("total") val total: Int
)