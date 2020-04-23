package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class TransactionsPaymentInfoResponseModel(@SerializedName("suid") val suid: String,
                                                @SerializedName("card_number")  val card_number: String,
                                                @SerializedName("payment_type")  val payment_type: String

                                              )

