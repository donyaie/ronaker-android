package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.model.Order
import io.reactivex.Observable
import java.util.*

interface OrderRepository {
    fun getOrders(token: String?, filter: String?): Observable<Result<ListResponseModel<Order>>>
    fun createOrder(
        token: String?,
        product_suid: String,
        stateDate: Date,
        endDate: Date,
        message: String?,
        price: Double
    ): Observable<Result<Boolean>>

    fun updateOrderStatus(
        token: String?,
        suid: String,
        status: String,
        address: String?=null,
        instruction: String?=null,
        reason: String?=null
    ): Observable<Result<Boolean>>

    fun orderRate(
        token: String?,
        orderSuid: String,
        comment: String,
        stars: Double
    ): Observable<Result<Boolean>>
}

