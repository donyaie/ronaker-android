package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName
import com.ronaker.app.model.User

data class UserInfoResponceModel(
    @SerializedName("suid") val suid: String
    ,@SerializedName("email") val email: String?
    ,@SerializedName("is_email_verified") val is_email_verified: Boolean?
    ,@SerializedName("first_name") val first_name: String?
    ,@SerializedName("last_name") val last_name: String?
    ,@SerializedName("phone_number") val phone_number: String?
    ,@SerializedName("is_phone_number_verified") val is_phone_number_verified: Boolean?
    ,@SerializedName("avatar") val avatar: String?
    ,@SerializedName("is_payment_info_verified") val is_payment_info_verified: Boolean?
    ,@SerializedName("is_identity_info_verified") val is_identity_info_verified: Boolean?
    ,@SerializedName("balance") val balance: Double?



)