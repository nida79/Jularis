package com.ekr.jularis.data.employee

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmployeeAdd(
    @SerializedName("username") val username: String,
    @SerializedName("full_name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String?,
    @SerializedName("no_telp") val noTelp: String,
    @SerializedName("address") val address: String
) : Parcelable