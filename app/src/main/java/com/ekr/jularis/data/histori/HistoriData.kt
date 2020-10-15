package com.ekr.jularis.data.histori


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoriData(
    @SerializedName("address") val address: String,
    @SerializedName("note") val note: String?,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("picture_transaction") val pictureTransaction: String,
    @SerializedName("product_amount") val productAmount: Int,
    @SerializedName("product_list") val product_list: List<HistoriProduct>,
    @SerializedName("quantity_total") val quantityTotal: Int,
    @SerializedName("service_price") val servicePrice: Int,
    @SerializedName("transaction_amount") val transactionAmount: Int,
    @SerializedName("transaction_date") val transactionDate: String,
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("transaction_invoice") val transactionInvoice: String,
    @SerializedName("transaction_photo_transfer") val transactionPhotoTransfer: String?,
    @SerializedName("transaction_state") val transactionState: String,
    @SerializedName("user_phone") val userPhone: String
) : Parcelable