package com.ekr.jularis.data.histori


import android.os.Parcelable
import com.ekr.jularis.data.histori.HistoriProduct
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoriData(
    @SerializedName("address") val address: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("note") val note: String,
    @SerializedName("picture_product_transaction") val pictureProductTransaction: String,
    @SerializedName("product") val historiProduct: HistoriProduct,
    @SerializedName("product_amount") val productAmount: Int,
    @SerializedName("product_id") val productId: String,
    @SerializedName("product_name") val productName: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("transaction_date") val transactionDate: String,
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("transaction_invoice") val transactionInvoice: String,
    @SerializedName("transaction_product_id") val transactionProductId: String,
    @SerializedName("transaction_photo_transfer") val transaction_photo_transfer: String?,
    @SerializedName("transaction_state") val transactionState: String,
    @SerializedName("user_id") val userId: Int
): Parcelable