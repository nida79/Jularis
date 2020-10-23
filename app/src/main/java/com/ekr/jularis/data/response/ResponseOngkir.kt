package com.ekr.jularis.data.response

import com.google.gson.annotations.SerializedName

data class ResponseOngkir(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String
)