package com.ronaker.app.data.network.response

data class ProductSearchResponceModel(val count: Int ,
                                      val next: String? ,
                                      val previous: String? ,
                                      val results: List<ProductItemResponceModel>? )


