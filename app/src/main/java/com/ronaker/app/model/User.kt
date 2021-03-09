package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.UserInfoResponceModel

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

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
    var suid: String? = null
    , var email: String? = null
    , var is_email_verified: Boolean = false
    , var first_name: String? = null
    , var last_name: String? = null
    , var phone_number: String? = null
    , var is_phone_number_verified: Boolean = false
    , var is_payment_info_verified: Boolean = false
    , var is_identity_info_verified: Boolean = false
    , var avatar: String? = null
    , var password: String? = null
    , var promotionCode: String? = null
    , var balance: Double? = null
    , var smart_id_national_code: String? = null
    , var smart_id_personal_code: String? = null
,  var completed:Int =0

) : Parcelable {
    constructor() : this(
        null
    )

    @IgnoredOnParcel
    var accessToken: String? = null


    fun isComplete(): Boolean {
        completed = 0


        if (!avatar.isNullOrBlank())
            completed++

//        if (!smart_id_personal_code.isNullOrBlank())
//            completed++

        if (is_email_verified) completed++
        if (is_phone_number_verified) completed++
//        user.is_payment_info_verified?.let { if (it) complete++ }
//        user.is_identity_info_verified?.let { if (it) complete++ }


        return completed == 3
    }

//
//    @IgnoredOnParcel
//    var completed = 0


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
        balance = balance,
        smart_id_national_code = smart_id_national_code,
        smart_id_personal_code = smart_id_personal_code
    )

}
