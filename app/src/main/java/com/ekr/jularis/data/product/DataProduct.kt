package com.ekr.jularis.data.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataProduct(
    @SerializedName("product_id") val product_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("product_discont_quantity") val product_discont_quantity: Int?,
    @SerializedName("product_discont_present") val product_discont_present: Int?,
    @SerializedName("ongkir") val ongkir: String?,
    @SerializedName("product_picture") val product_picture: List<DataImageProduct>
) : Parcelable