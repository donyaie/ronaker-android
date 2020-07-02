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
 * @property phone_number the Checkout of user
 * @property is_phone_number_verified if Checkout verified is true
 */
@Parcelize
data class User(
    var suid: String?=null
    , var email: String?=null
    , var is_email_verified: Boolean=false
    , var first_name: String?=null
    , var last_name: String?=null
    , var phone_number: String?=null
    , var is_phone_number_verified: Boolean=false
    , var is_payment_info_verified: Boolean=false
    , var is_identity_info_verified: Boolean=false
    , var avatar: String?=null
    , var password: String? = null
    , var promotionCode: String? = null
    , var balance: Double = 0.0
    ,val smart_id_national_code:String?=null
    ,val smart_id_personal_code:String?=null

) : Parcelable {
    constructor() : this(
        null
    )

    @IgnoredOnParcel
    var accessToken: String? = null


}


fun UserInfoResponceModel.toUserModel(): User {


    smart_id_national_code
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
        balance = balance,
        smart_id_national_code = smart_id_national_code,
        smart_id_personal_code = smart_id_personal_code
    )

}
