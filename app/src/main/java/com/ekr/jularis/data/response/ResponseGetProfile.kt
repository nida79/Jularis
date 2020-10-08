package com.ekr.jularis.data.response


import com.ekr.jularis.data.profile.DataGet
import com.google.gson.annotations.SerializedName

data class ResponseGetProfile(
    @SerializedName("data") val data: DataGet,
    @SerializedName("status") val status: Boolean
)