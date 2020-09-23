package com.ekr.jularis.data.response

import com.ekr.jularis.data.login.DataLogin
import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DataLogin?
)