package com.ekr.jularis.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseNewHistori(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("data") val data: List<HistoriNewData>,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?
) : Parcelable