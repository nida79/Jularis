package com.ekr.jularis.data.payment


import com.google.gson.annotations.SerializedName

data class DataGetPayment(
    @SerializedName("checked") val checked: Int,
    @SerializedName("checkout_amount") val checkoutAmount: Int,
    @SerializedName("checkout_id") val checkoutId: String,
    @SerializedName("picture") val picturePayment: PicturePayment,
    @SerializedName("product") val productPayment: ProductPayment,
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("user_id") val userId: Int
)