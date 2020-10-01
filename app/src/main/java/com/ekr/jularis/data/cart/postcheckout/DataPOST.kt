package com.ekr.jularis.data.cart.postcheckout


import com.google.gson.annotations.SerializedName

data class DataPOST(
    @SerializedName("data") val data: List<Data>
)