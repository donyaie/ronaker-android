package com.ronaker.app.data.network.request

data class UserActivePhoneRequestModel(
    val phone_number: String
    , val activation_code: String
)