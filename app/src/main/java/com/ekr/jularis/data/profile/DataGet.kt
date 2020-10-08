package com.ekr.jularis.data.profile


import com.google.gson.annotations.SerializedName

data class DataGet(
    @SerializedName("address") val address: String,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("no_telp") val noTelp: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("username") val username: String
)