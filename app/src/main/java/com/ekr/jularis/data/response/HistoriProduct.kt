package com.ekr.jularis.data.response


import com.google.gson.annotations.SerializedName

data class HistoriProduct(
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("name") val name: String,
    @SerializedName("ongkir") val ongkir: Int,
    @SerializedName("price") val price: Int,
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int
)