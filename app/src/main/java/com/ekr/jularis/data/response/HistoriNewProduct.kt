package com.ekr.jularis.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoriNewProduct(
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_picture") val productPicture: String,
    @SerializedName("quantity") val quantity: Int
) : Parcelable