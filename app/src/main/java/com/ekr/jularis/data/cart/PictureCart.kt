package com.ekr.jularis.data.cart


import com.google.gson.annotations.SerializedName

data class PictureCart(
    @SerializedName("picture") val picture: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("product_picture_id") val productPictureId: String
)