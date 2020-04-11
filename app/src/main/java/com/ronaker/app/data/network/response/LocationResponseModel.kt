package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class LocationResponseModel(@SerializedName("lat") val lat: Double,
                                 @SerializedName("lng") val lng: Double)


