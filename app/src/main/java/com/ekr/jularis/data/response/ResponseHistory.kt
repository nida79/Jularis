package com.ekr.jularis.data.response


import com.google.gson.annotations.SerializedName

data class ResponseHistory(
    @SerializedName("data") val data: List<HistoriData>,
    @SerializedName("last_page") val last_page: Int,
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String
)