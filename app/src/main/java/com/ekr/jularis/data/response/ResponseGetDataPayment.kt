package com.ekr.jularis.data.response


import com.ekr.jularis.data.payment.DataGetPayment
import com.google.gson.annotations.SerializedName

data class ResponseGetDataPayment(
    @SerializedName("checked_amount") val checkedAmount: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("data") val data: List<DataGetPayment>,
    @SerializedName("ongkir") val ongkir: Int,
    @SerializedName("transaction_amount") val transactionAmount: Int
)