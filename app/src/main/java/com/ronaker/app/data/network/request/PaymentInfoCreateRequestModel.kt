package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName
import java.util.*

data class PaymentInfoCreateRequestModel(@SerializedName("card_number") val card_number: String?,
                                         @SerializedName("expiry_month") val expiry_month: String?,
                                         @SerializedName("expiry_year") val expiry_year: String?,
                                         @SerializedName("postal_code") val postal_code: String?,
                                         @SerializedName("full_name") val full_name: String?,
                                         @SerializedName("address") val address: String?,
                                         @SerializedName("address_2") val address_2: String?,
                                         @SerializedName("country") val country: String?,
                                         @SerializedName("region") val region: String?,
                                         @SerializedName("city") val city: String?,
                                         @SerializedName("cvv") val cvv: String?,
                                         @SerializedName("payment_info_type") val payment_info_type: String?


)


