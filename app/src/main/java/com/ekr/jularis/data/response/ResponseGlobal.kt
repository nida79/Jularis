package com.ekr.jularis.data.response

import com.ekr.jularis.data.cart.postcheckout.Data
import com.google.gson.annotations.SerializedName

data class ResponseGlobal(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Data?

)