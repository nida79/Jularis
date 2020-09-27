package com.ekr.jularis.data.postcheckout


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("checked")
    val checked: Int,
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int
)