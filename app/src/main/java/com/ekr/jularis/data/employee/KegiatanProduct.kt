package com.ekr.jularis.data.employee


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KegiatanProduct(
    @SerializedName("note") val note: String,
    @SerializedName("price") val price: Int,
    @SerializedName("product_id") val productId: String,
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_picture") val productPicture: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("transaction_product_id") val transactionProductId: String
) : Parcelable