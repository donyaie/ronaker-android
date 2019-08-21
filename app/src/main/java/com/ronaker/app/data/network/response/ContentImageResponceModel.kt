package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class ContentImageResponceModel(@SerializedName("suid") val suid: String,
                                     @SerializedName("created_at")  val created_at: String,
                                     @SerializedName("content")  val content: String )
