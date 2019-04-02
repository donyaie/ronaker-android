package com.ronaker.app.network.request

data class UserActivePhoneRequestModel(
    val phone_number: String
    , val activation_code: String
)