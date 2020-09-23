package com.ekr.jularis.data.login

import com.google.gson.annotations.SerializedName

data class DataLogin(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val full_name: String,
    @SerializedName("no_telp") val no_telp: String,
    @SerializedName("address") val address: String,
    @SerializedName("token") val token : String,
    @SerializedName("photo") val photo : String,
    @SerializedName("role") val role: String
)