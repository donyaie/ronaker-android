package com.ronaker.app.data.network.request

import java.util.*

data class OrderCreateRequestModel(val product_suid: String,
                                   val start_date: Date,
                                   val end_date: Date,
                                   val message: String?)


