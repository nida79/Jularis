package com.ekr.jularis.data.response


import com.google.gson.annotations.SerializedName

data class ResponseBalanced(
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: Boolean,
    @SerializedName("total_amount") val totalAmount: Int
)