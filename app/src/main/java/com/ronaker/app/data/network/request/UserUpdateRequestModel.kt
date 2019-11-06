package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserUpdateRequestModel(@SerializedName("first_name") val first_name: String?,
                                  @SerializedName("last_name") val last_name: String?,
                                  @SerializedName("email") val email: String?,
                                  @SerializedName("avatar") val avatar: String?
)


