package com.ekr.jularis.data.histori


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoriProduct(
    @SerializedName("product_name") val product_name: String,
    @SerializedName("product_picture") val product_picture: String,
    @SerializedName("price") val price: Int,
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int
): Parcelable