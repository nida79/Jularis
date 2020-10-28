package com.ekr.jularis.data.response

import com.ekr.jularis.data.product.GetDataOngkir
import com.ekr.jularis.data.product.OngkirData
import com.google.gson.annotations.SerializedName

data class ResponseOngkir(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val ongkirData: List<GetDataOngkir>?,
)