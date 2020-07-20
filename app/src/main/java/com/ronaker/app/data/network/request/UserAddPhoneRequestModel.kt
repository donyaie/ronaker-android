package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserAddPhoneRequestModel(
    @SerializedName("phone_number") val phone_number: String
)