package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.UserInfoResponceModel
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

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
@Parcelize
data class User(
    var suid: String?
    , var email: String?
    , var is_email_verified: Boolean?
    , var first_name: String?
    , var last_name: String?
    , var phone_number: String?
    , var is_phone_number_verified: Boolean?
    , var is_payment_info_verified: Boolean?
    , var is_identity_info_verified: Boolean?
    , var avatar: String?
    , var password: String? = null
    , var promotionCode: String? = null
    , val balance: Double? = null
) : Parcelable {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    @IgnoredOnParcel
    var accessToken: String? = null


}


fun UserInfoResponceModel.toUserModel(): User {


    return User(
        suid = suid,
        email = email,
        is_email_verified = is_email_verified,
        first_name = first_name,
        last_name = last_name,
        phone_number = phone_number,
        is_phone_number_verified = is_phone_number_verified,
        is_payment_info_verified = is_payment_info_verified,
        is_identity_info_verified = is_identity_info_verified,
        avatar = avatar,
        balance = balance


    )

}
