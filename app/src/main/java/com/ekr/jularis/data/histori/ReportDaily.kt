package com.ekr.jularis.data.histori

import com.google.gson.annotations.SerializedName

class ReportDaily(
    @SerializedName("start_date") val start_date: String,
    @SerializedName("end_date") val end_date: String,
)