package com.ronaker.app.data


import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.FreeResponseModel
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.data.network.response.OrderResponseModel
import io.reactivex.Observable
import java.util.*

interface OrderRepository {
    fun getOrders(token: String?, filter: String?): Observable<Result<ListResponseModel<OrderResponseModel>>>
    fun createOrder(token: String?, product_suid: String, stateDate: Date, endDate: Date, message: String?, price: Double): Observable<Result<FreeResponseModel>>
    fun updateOrderStatus(token: String?, suid: String, status: String): Observable<Result<FreeResponseModel>>
}

