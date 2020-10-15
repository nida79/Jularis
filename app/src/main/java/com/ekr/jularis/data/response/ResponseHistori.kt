package com.ekr.jularis.data.response


import android.os.Parcelable
import com.ekr.jularis.data.histori.HistoriData
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseHistori(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("data") val data: List<HistoriData>,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?
) : Parcelable