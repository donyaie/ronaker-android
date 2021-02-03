package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class StripeSetupResponseModel(
    @SerializedName("is_ready") val is_ready: Boolean?
    , @SerializedName("link") val link: String?
)

