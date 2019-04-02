package com.ronaker.app.model

/**
 * Class which provides a model for User
 * @constructor Sets all properties of the post
 * @property suid the unique identifier of the user
 * @property email the unique email Address
 * @property is_email_verified if email verified is true
 * @property first_name the first name of user
 * @property phone_number the number of user
 * @property is_phone_number_verified if number verified is true
 */
data class User(val suid: String?
                , val email: String?
                , val is_email_verified: Boolean?
                , val first_name: String?
                , val phone_number: String?
                , val is_phone_number_verified:Boolean?
)