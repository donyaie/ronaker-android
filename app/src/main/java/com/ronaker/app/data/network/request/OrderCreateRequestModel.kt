package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName
import java.util.*

data class OrderCreateRequestModel(
    @SerializedName("product_suid") val product_suid: String,
    @SerializedName("start_date") val start_date: Date,
    @SerializedName("end_date") val end_date: Date,
    @SerializedName("message") val message: String?,
    @SerializedName("price") val price: Double

)


