package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class UserInfoResponceModel(
    @SerializedName("suid") val suid: String
    , @SerializedName("email") val email: String?
    , @SerializedName("is_email_verified") val is_email_verified: Boolean
    , @SerializedName("first_name") val first_name: String?
    , @SerializedName("last_name") val last_name: String?
    , @SerializedName("phone_number") val phone_number: String?
    , @SerializedName("is_phone_number_verified") val is_phone_number_verified: Boolean
    , @SerializedName("avatar") val avatar: String?
    , @SerializedName("is_payment_info_verified") val is_payment_info_verified: Boolean
    , @SerializedName("is_identity_info_verified") val is_identity_info_verified: Boolean
    , @SerializedName("balance") val balance: Double?
    , @SerializedName("smart_id_national_code") val smart_id_national_code: String?
    , @SerializedName("smart_id_personal_code") val smart_id_personal_code: String?
    , @SerializedName("docusign_is_last_valid") val docusign_is_last_valid: Boolean=false



)