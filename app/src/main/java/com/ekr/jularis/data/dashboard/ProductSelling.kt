package com.ekr.jularis.data.dashboard


import com.google.gson.annotations.SerializedName

data class ProductSelling(
    @SerializedName("product_id") val productId: String,
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_picture") val product_picture:String,
    @SerializedName("total") val total: Int
)