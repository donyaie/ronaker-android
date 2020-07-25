package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserForgetPasswordConfirmRequestModel(
    @SerializedName("token") val token: String
    , @SerializedName("password") val password: String
)