package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName


data class ProductRateRequestModel(@SerializedName("stars") val stars: Int,
                                   @SerializedName("comment") val comment: String)



