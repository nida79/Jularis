package com.ekr.jularis.data.response


import com.google.gson.annotations.SerializedName

data class ProductSelling(
    @SerializedName("product_id") val productId: String,
    @SerializedName("product_name") val productName: String,
    @SerializedName("total") val total: Int
)