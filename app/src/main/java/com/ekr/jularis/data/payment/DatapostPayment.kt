package com.ekr.jularis.data.payment


import com.google.gson.annotations.SerializedName

data class DatapostPayment(
    @SerializedName("data") val dataPayment: List<DataPayment>,
    @SerializedName("address") val address: String,
    @SerializedName("count") val count: Int,
    @SerializedName("note") val note: String?,
    @SerializedName("checked_amount") val checked_amount: Int,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("ongkir") val ongkir: Int,
    @SerializedName("transaction_amount") val transaction_amount: Int,
    @SerializedName("transaction_photo_id") val transactionPhotoId: String?,
    @SerializedName("user_phone") val user_phone: String
)