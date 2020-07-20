package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class ProductItemImageResponceModel(
    @SerializedName("url") val url: String,
    @SerializedName("suid") val suid: String
)
