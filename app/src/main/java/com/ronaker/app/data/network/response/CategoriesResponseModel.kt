package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponseModel(
    @SerializedName("suid") val suid: String,
    @SerializedName("title") val title: String?,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("sub_categories") val sub_categories: List<CategoriesResponseModel>?
)
