package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.OrderCreateRequestModel
import com.ronaker.app.data.network.request.OrderUpdateRequestModel
import com.ronaker.app.data.network.request.ProductRateRequestModel
import com.ronaker.app.data.network.response.FreeResponseModel
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.data.network.response.OrderResponseModel
import io.reactivex.Observable
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface OrderApi {
    /**
     * get Orders
     */
    @GET("/api/v1/orders/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getOrders(
        @Header("Authorization") authToken: String,
        @Query("filter") filter: String?
    ): Observable<ListResponseModel<OrderResponseModel>>


    /**
     * create Order
     */
    @POST("/api/v1/orders/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun createOrder(
        @Header("Authorization") authToken: String,
        @Body request: OrderCreateRequestModel
    ): Observable<FreeResponseModel>


    /**
     * update Order State
     */
    @PUT("/api/v1/orders/{order_suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun updateOrderStatus(
        @Header("Authorization") authToken: String,
        @Path("order_suid") suid: String,
        @Body request: OrderUpdateRequestModel
    ): Observable<FreeResponseModel>


    /**
     * get Order Detail
     */
    @GET("/api/v1/orders/{order_suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getOrderDetail(
        @Header("Authorization") authToken: String,
        @Path("order_suid") suid: String
    ): Observable<OrderResponseModel>


    /**
     * Order Rate
     */
    @POST("/api/v1/orders/{order_suid}/rate")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun orderRate(
        @Header("Authorization") authToken: String,
        @Path("order_suid") suid: String,
        @Body request: ProductRateRequestModel
    ): Observable<FreeResponseModel>


}