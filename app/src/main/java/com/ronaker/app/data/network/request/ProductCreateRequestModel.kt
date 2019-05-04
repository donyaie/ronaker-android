package com.ronaker.app.data.network.request

data class ProductCreateRequestModel(val name: String,
                                     val price_per_day: Double,
                                     val price_per_week: Double,
                                     val price_per_month: Double,
                                     val description: String,
                                     val new_avatar_suid: String,
                                     val new_images: ArrayList<String>?)


