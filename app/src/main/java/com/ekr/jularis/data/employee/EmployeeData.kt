package com.ekr.jularis.data.employee


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmployeeData(
    @SerializedName("address") val address: String,
    @SerializedName("email") val email: String,
    @SerializedName("firebase_token") val firebaseToken: String,
    @SerializedName("id") val id: Int,
    @SerializedName("is_online") val isOnline: Int,
    @SerializedName("name") val name: String,
    @SerializedName("no_telp") val noTelp: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("role") val role: String,
    @SerializedName("total_checkout") val totalCheckout: Int,
    @SerializedName("username") val username: String
) : Parcelable