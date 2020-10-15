package com.ekr.jularis.data.response


import com.google.gson.annotations.SerializedName

data class ResponseSellingtoday(
    @SerializedName("product_selling") val productSelling: List<ProductSelling>,
    @SerializedName("transaction_amount_today") val transactionAmountToday: Int
)