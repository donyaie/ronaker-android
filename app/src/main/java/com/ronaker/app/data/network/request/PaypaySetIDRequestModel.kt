package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName


data class PaypaySetIDRequestModel(
    @SerializedName("payment_id") val payment_id: String,
)



