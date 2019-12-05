package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.*
import com.ronaker.app.data.network.response.*
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
    fun getOrders(@Header("Authorization") authToken: String,@Query("filter") filter:String?): Observable<ListResponseModel<OrderResponseModel>>



    /**
     * create Order
     */
    @POST("/api/v1/orders/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun createOrder(@Header("Authorization") authToken: String,@Body request: OrderCreateRequestModel): Observable<FreeResponseModel>


    /**
     * update Order
     */
    @PUT("/api/v1/orders/{order_suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun updateOrderStatus(@Header("Authorization") authToken: String, @Path("order_suid") suid:String, @Body request: OrderUpdateRequestModel): Observable<FreeResponseModel>



    /**
     * create Order
     */
    @POST("/api/v1/orders/{order_suid}/rate")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun orderRate(@Header("Authorization") authToken: String, @Path("order_suid") suid:String, @Body request: ProductRateRequestModel): Observable<FreeResponseModel>





}