package com.ekr.jularis.data.product


import com.google.gson.annotations.SerializedName

data class OngkirData(
    @SerializedName("0-2") val nol_dua: String?,
    @SerializedName("2-5") val dua_lima: String?,
    @SerializedName("5-10") val lima_sepuluh: String?,
    @SerializedName("10<") val lebihdari_sepuluh: String?
)