package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class UserGoogleLoginResponseModel(
    @SerializedName("token") val token: String
)