package com.ronaker.app.data.network.response

data class ProductItemResponceModel(
    val suid: String,
    val name: String,
    val price_per_day: Double,
    val price_per_week: Double,
    val price_per_month: Double,
    val description: String,
    val avatar: String,
    val images: List<ProductItemImageResponceModel>?,
    val categories: List<CategoriesResponseModel>?
)
