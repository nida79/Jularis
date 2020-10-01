package com.ekr.jularis.data.payment

import com.google.gson.annotations.SerializedName

data class PaymentPhoto(
    @SerializedName("transaction_photo_id") val transaction_photo_id: String
)