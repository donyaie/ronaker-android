package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class UserSmartIdVerificationResponseModel(@SerializedName("verification_code") val verification_code: String)