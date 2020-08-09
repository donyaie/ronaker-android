package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserSmartIdVerificationCodeRequestModel(
    @SerializedName("national_code") val national_code: String
    , @SerializedName("personal_code") val personal_code: String
)