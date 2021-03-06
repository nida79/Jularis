package com.ekr.jularis.data.cart


import com.google.gson.annotations.SerializedName

data class Checkout(
    @SerializedName("checkout_amount") val checkout_amount: Int,
    @SerializedName("checked") val checked: String,
    @SerializedName("checkout_id") val checkoutId: String,
    @SerializedName("picture") val pictureCart: PictureCart,
    @SerializedName("product") val productCart: ProductCart,
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("user_id") val userId: Int
)