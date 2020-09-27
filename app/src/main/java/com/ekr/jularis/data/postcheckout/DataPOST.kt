package com.ekr.jularis.data.postcheckout


import com.google.gson.annotations.SerializedName

data class DataPOST(
    @SerializedName("data")
    val data: List<Data>
)