package com.ekr.jularis.data.profile


import com.google.gson.annotations.SerializedName

data class DataProfile(
    @SerializedName("address") val address: String,
    @SerializedName("firebase_token") val firebaseToken: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("no_telp") val noTelp: String
)