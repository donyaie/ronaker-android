package com.ronaker.app.data.network.response

import com.ronaker.app.model.User

data class UserInfoResponceModel(
    val suid: String
    , val email: String
    , val is_email_verified: Boolean?
    , val first_name: String
    , val last_name: String
    , val phone_number: String?
    , val is_phone_number_verified: Boolean?
){

    fun map():User{
        return User(suid,email,is_email_verified,first_name,last_name,phone_number,is_phone_number_verified)
    }
}