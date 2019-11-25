package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class UserRegisterResponseModel(   @SerializedName("token") val token: String,
                                        @SerializedName("user") val user: UserInfoResponceModel)