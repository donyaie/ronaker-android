package com.ronaker.app.data.network.request

import com.google.gson.annotations.SerializedName

data class ProductCreateRequestModel(@SerializedName("name") val name: String?,
                                     @SerializedName("price_per_day")   val price_per_day: Double?,
                                     @SerializedName("price_per_week")   val price_per_week: Double?,
                                     @SerializedName("price_per_month")   val price_per_month: Double?,
                                     @SerializedName("description")   val description: String?,
                                     @SerializedName("new_avatar_suid")   val new_avatar_suid: String?,
                                     @SerializedName("new_images")   val new_images: ArrayList<String>?,
                                     @SerializedName("new_categories")   val new_categories: ArrayList<String>?)


