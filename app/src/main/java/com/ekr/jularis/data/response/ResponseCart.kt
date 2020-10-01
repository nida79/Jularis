package com.ekr.jularis.data.response


import com.ekr.jularis.data.cart.Checkout
import com.google.gson.annotations.SerializedName

data class ResponseCart(
    @SerializedName("checkout") val checkout: List<Checkout>?,
    @SerializedName("total_amount") val total_amount: Int?,
    @SerializedName("checked_amount") val checked_amount: Int,
    @SerializedName("transaction_amount") val transaction_amount: Int,
    @SerializedName("ongkir") val ongkir: Int,
    @SerializedName("count") val count: Int
)