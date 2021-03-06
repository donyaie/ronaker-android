package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class OrderResponseModel(
    @SerializedName("suid") val suid: String,
    @SerializedName("prices") val prices: List<OrderPriceResponseModel>?,
    @SerializedName("start_date") val start_date: Date,
    @SerializedName("end_date") val end_date: Date,
    @SerializedName("message") val message: String,
    @SerializedName("order_type") val order_type: String,
    @SerializedName("product") val product: ProductItemResponseModel,
    @SerializedName("status") val status: String,
    @SerializedName("order_user") val order_user: UserInfoResponceModel?,
    @SerializedName("product_owner") val product_owner: UserInfoResponceModel?,
    @SerializedName("rejection_reason") val rejection_reason: String,
    @SerializedName("address") val address: String,
    @SerializedName("instruction") val instruction: String,
    @SerializedName("is_archived") val is_archived: Boolean = false,
    @SerializedName("smart_id_owner_session_id") val smart_id_owner_session_id: String ?,
    @SerializedName("smart_id_creator_session_id") val smart_id_creator_session_id: String?,
    @SerializedName("sign_pdf") val sign_pdf: OrderResponsePDFSignModel?,


    @SerializedName("is_charge_placed") val is_charge_placed: Boolean=false,

    @SerializedName("envelope_id") val envelope_id: String?,
    @SerializedName("lister_signed") val lister_signed: Boolean=false,
    @SerializedName("renter_signed") val renter_signed: Boolean=false,
    @SerializedName("latest_signed_pdf") val latest_signed_pdf: OrderResponsePDFSignModel?,










)
