package com.ekr.jularis.data.login

import com.google.gson.annotations.SerializedName

data class LoginGoogle(
    @SerializedName("email") val email:String,
    @SerializedName("full_name") val full_name:String
)