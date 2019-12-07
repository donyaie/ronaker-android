package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class OrderPriceResponseModel(@SerializedName("key") val key: String,
                                   @SerializedName("price")  val price: Double
                              )
