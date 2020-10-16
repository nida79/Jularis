package com.ekr.jularis.data.response

import com.ekr.jularis.data.dashboard.ProductSelling
import com.google.gson.annotations.SerializedName

data class ResponseTopselling(
    @SerializedName("status") val status : Boolean,
    @SerializedName("message") val message:String?,
    @SerializedName("data") val data:List<ProductSelling>?
)