package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class InitialPaymentResponseModel(
    @SerializedName("client_secret") val client_secret: String,

)

