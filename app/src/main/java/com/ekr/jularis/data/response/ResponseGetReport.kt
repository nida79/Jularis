package com.ekr.jularis.data.response


import com.google.gson.annotations.SerializedName

data class ResponseGetReport(
    @SerializedName("status") val status: Boolean,
    @SerializedName("url") val url: String
)