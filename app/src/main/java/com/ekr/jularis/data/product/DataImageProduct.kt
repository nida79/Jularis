package com.ekr.jularis.data.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataImageProduct(
    @SerializedName("picture") val picture: String,
    @SerializedName("product_id") val product_id: String,
    @SerializedName("product_picture_id") val product_picture_id: String
) : Parcelable