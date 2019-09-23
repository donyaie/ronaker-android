package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserIdentifyRequestModel(
    @SerializedName("image_suid") val image_suid: String
    ,@SerializedName("document_type") val document_type: String
)