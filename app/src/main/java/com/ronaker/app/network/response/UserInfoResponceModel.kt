package com.ronaker.app.network.response

data class UserInfoResponceModel(
    val suid: String
    , val email: String
    , val is_email_verified: Boolean?
    , val first_name: String
    , val last_name: String
    , val phone_number: String?
    , val is_phone_number_verified: Boolean?
)