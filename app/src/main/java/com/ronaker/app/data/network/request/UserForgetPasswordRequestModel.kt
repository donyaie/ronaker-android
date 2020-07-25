package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserForgetPasswordRequestModel(
    @SerializedName("email") val email: String
)