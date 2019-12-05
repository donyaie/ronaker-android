package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class ProductRatingResponceModel(
    @SerializedName("stars") val stars: Int,
    @SerializedName("comment") val comment: String
) {


}