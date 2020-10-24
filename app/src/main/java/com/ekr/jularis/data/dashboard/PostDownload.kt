package com.ekr.jularis.data.dashboard

import com.google.gson.annotations.SerializedName

data class PostDownload(
    @SerializedName("month") val month: String,
    @SerializedName("year") val year: String
)