package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class ProductDetailResponseModel(
    @SerializedName("suid") val suid: String,
    @SerializedName("name") val name: String,
    @SerializedName("price_per_day") val price_per_day: Double = 0.0,
    @SerializedName("price_per_week") val price_per_week: Double = 0.0,
    @SerializedName("price_per_month") val price_per_month: Double = 0.0,
    @SerializedName("description") val description: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("avatar_suid") val avatar_suid: String,
    @SerializedName("owner") val owner: UserInfoResponceModel?,
    @SerializedName("categories") val categories: List<CategoriesResponseModel>?,
    @SerializedName("images") val images: List<ProductItemImageResponceModel>?,
    @SerializedName("location") val location: LocationResponseModel?,
    @SerializedName("address") val address: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("review_status") val review_status: String?,
    @SerializedName("user_status") val user_status: String?,
    @SerializedName("insurance_image") val insurance_image: String?,
    @SerializedName("is_favourite") val is_favourite: Boolean = false


)
