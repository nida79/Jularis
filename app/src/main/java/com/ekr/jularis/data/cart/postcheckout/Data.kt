package com.ekr.jularis.data.cart.postcheckout


import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("checked") var checked: Int,
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int
)