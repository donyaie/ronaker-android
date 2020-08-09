package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserActivePhoneRequestModel(
    @SerializedName("phone_number") val phone_number: String
    , @SerializedName("activation_code") val activation_code: String
)