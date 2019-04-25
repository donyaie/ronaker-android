package com.ronaker.app.data.network.request

data class UserRegisterRequestModel(  val email: String
                                     , val password: String
                                     , val first_name: String
                                     , val last_name: String)