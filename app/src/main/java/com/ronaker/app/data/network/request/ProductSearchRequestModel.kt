package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName
import com.ronaker.app.data.network.response.LocationResponseModel

data class ProductSearchRequestModel(
    @SerializedName("name") val name: String?,
    @SerializedName("location") val location: LocationResponseModel?,
    @SerializedName("radius") val radius: Int?,
    @SerializedName("favourites") val favourites: Boolean?
)


