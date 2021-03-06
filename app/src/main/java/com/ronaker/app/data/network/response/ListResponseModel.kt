package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class ListResponseModel<T>(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<T>?
)



