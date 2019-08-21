package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class ProductOwnerResponceModel(@SerializedName("suid") val suid: String,
                                     @SerializedName("first_name")  val first_name: String,
                                     @SerializedName("last_name")  val last_name: String )
