package com.ekr.jularis.data.payment

import com.google.gson.annotations.SerializedName


data class DataAlamat(
    @SerializedName("address") val address: String
)