package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName
import java.util.*

data class CheckPaymentRequestModel(
    @SerializedName("payment_id") val payment_id: String,

)

