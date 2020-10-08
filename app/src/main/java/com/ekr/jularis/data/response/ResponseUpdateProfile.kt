package com.ekr.jularis.data.response


import com.ekr.jularis.data.profile.DataProfile
import com.google.gson.annotations.SerializedName

data class ResponseUpdateProfile(
    @SerializedName("data") val data: DataProfile,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Boolean
)