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
    @SerializedName("service_price") val service_price: Int,
    @SerializedName("payment_method") val payment_method: String,
    @SerializedName("picture_transaction") val picture_transaction: String,
    @SerializedName("product_list") val historiProduct: HistoriProduct,
    @SerializedName("product_amount") val productAmount: Int,
    @SerializedName("quantity_total") val quantity: Int,
    @SerializedName("transaction_date") val transactionDate: String,
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("transaction_invoice") val transactionInvoice: String,
    @SerializedName("transaction_photo_transfer") val transaction_photo_transfer: String?,
    @SerializedName("transaction_state") val transactionState: String,
): Parcelable