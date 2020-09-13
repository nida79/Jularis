package com.ekr.jularis.data.login

import com.ekr.jularis.data.login.DataLogin
import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("status") val status: Boolean,
    @SerializedName("msg") val msg: String,
    @SerializedName("pegawai") val pegawai: DataLogin?
)