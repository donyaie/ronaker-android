package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class UserAddPhoneResponceModel(
    @SerializedName("phone_number") val phone_number: String
)