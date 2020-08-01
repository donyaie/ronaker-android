package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.model.Order
import io.reactivex.Observable
import java.util.*

interface OrderRepository {


    fun getOrders(
        token: String?,
        page: Int,
        filter: String?
    ): Observable<Result<ListResponseModel<Order>>>

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
        status: String? = null,
        address: String? = null,
        instruction: String? = null,
        reason: String? = null,
        isArchived: Boolean? = null
    ): Observable<Result<Boolean>>

    fun orderRate(
        token: String?,
        orderSuid: String,
        comment: String,
        stars: Double
    ): Observable<Result<Boolean>>

    fun getOrderDetail(token: String?, suid: String): Observable<Result<Order>>


    fun startSmartIDAuth(
        user_token: String?,
        orderSuid: String
    ): Observable<Result<Boolean>>


    fun checkSmartIDSession(user_token: String?, orderSuid: String): Observable<Result<Boolean>>
    fun getSmartIDVerificationCode(
        user_token: String?,
        orderSuid: String
    ): Observable<Result<String>>

    fun startSmartIDCert(user_token: String?, orderSuid: String): Observable<Result<Boolean>>
    fun checkSmartIDSessionCert(user_token: String?, orderSuid: String): Observable<Result<Boolean>>
}

