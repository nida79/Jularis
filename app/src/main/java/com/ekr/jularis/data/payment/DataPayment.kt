package com.ekr.jularis.data.payment


import com.google.gson.annotations.SerializedName

data class DataPayment(
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int
)