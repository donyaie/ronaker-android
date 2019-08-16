package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class OrderResponseModel(@SerializedName("suid") val suid: String,
                              @SerializedName("price")  val price: Double,
                              @SerializedName("start_date")  val start_date: Date,
                              @SerializedName("end_date")  val end_date: Date,
                              @SerializedName("message")  val message: String,
                              @SerializedName("order_type")  val order_type: String,
                              @SerializedName("product")  val product: ProductItemResponceModel,
                              @SerializedName("status")  val status: String)
