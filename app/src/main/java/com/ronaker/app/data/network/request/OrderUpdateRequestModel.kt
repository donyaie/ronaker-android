package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName


data class OrderUpdateRequestModel( @SerializedName("status") val status: String)


