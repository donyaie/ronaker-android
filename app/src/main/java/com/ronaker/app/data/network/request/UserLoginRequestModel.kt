package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class UserLoginRequestModel( @SerializedName("email") val email: String
                                 , @SerializedName("password")  val password: String)