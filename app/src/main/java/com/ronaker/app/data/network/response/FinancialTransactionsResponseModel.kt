package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class FinancialTransactionsResponseModel(@SerializedName("suid") val suid: String,
                                              @SerializedName("amount")  val amount: Double,
                                              @SerializedName("transaction_type")  val transaction_type: String,
                                              @SerializedName("transaction_status")  val transaction_status: String,
                                              @SerializedName("description")  val description: String)
