package com.ekr.jularis.data.product


import com.google.gson.annotations.SerializedName

data class GetDataOngkir(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("key")
    val key: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("value")
    val value: Int
)