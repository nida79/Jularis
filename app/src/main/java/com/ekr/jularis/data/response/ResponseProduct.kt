package com.ekr.jularis.data.response

import com.ekr.jularis.data.product.DataProduct
import com.google.gson.annotations.SerializedName

data class ResponseProduct(
    @SerializedName("last_page") val last_page:Int?,
    @SerializedName("current_page") val current_page:Int,
    @SerializedName("data") val data : List<DataProduct>?
)