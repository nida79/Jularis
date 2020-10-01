package com.ekr.jularis.data.response


import com.ekr.jularis.data.payment.PaymentPhoto
import com.google.gson.annotations.SerializedName

data class ResponsePhotopayment(
    @SerializedName("data") val data: PaymentPhoto,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Boolean
)