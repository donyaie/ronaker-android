package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserRegisterRequestModel(
    @SerializedName("email") val email: String?
    , @SerializedName("password") val password: String?
    , @SerializedName("first_name") val first_name: String?
    , @SerializedName("last_name") val last_name: String?
    , @SerializedName("promotion_code") val promotion_code: String?
)

