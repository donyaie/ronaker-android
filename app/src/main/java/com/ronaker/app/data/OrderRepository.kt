package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.model.Order
import io.reactivex.Observable
import java.util.*

interface OrderRepository {


    fun getOrders(
        page: Int,
        filter: String?
    ): Observable<Result<ListResponseModel<Order>>>

    fun createOrder(
        product_suid: String,
        stateDate: Date,
        endDate: Date,
        message: String,
        price: Double
    ): Observable<Result<Boolean>>

    fun updateOrderStatus(
        suid: String,
        status: String? = null,
        address: String? = null,
        instruction: String? = null,
        reason: String? = null,
        isArchived: Boolean? = null
    ): Observable<Result<Boolean>>

    fun orderRate(
        orderSuid: String,
        comment: String,
        stars: Double
    ): Observable<Result<Boolean>>

    fun getOrderDetail( suid: String): Observable<Result<Order>>


    fun startSmartIDAuth(
        orderSuid: String
    ): Observable<Result<Boolean>>


    fun checkSmartIDSession(orderSuid: String): Observable<Result<Boolean>>
    fun getSmartIDVerificationCode(
        orderSuid: String
    ): Observable<Result<String>>

    fun startSmartIDCert( orderSuid: String): Observable<Result<Boolean>>
    fun checkSmartIDSessionCert( orderSuid: String): Observable<Result<Boolean>>
}

