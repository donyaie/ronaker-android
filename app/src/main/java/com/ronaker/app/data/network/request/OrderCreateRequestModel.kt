package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName
import java.util.*

data class OrderCreateRequestModel(
    @SerializedName("product_suid") val product_suid: String,
    @SerializedName("start_date") val start_date: String,
    @SerializedName("end_date") val end_date: String,
    @SerializedName("message") val message: String,
    @SerializedName("price") val price: Double

)


