package com.ekr.jularis.data.product


import com.google.gson.annotations.SerializedName

data class OngkirData(
    @SerializedName("0-2") val nol_dua: Int?,
    @SerializedName("2-5") val dua_lima: Int?,
    @SerializedName("5-10") val lima_sepuluh: Int?,
    @SerializedName("10<") val lebihdari_sepuluh: Int?
)