package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.CheckPaymentRequestModel
import com.ronaker.app.data.network.request.OrderCreateRequestModel
import com.ronaker.app.data.network.request.OrderUpdateRequestModel
import com.ronaker.app.data.network.request.ProductRateRequestModel
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
    fun getOrders(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Query("page") page: Int,
        @Query("filter") filter: String?
    ): Observable<ListResponseModel<OrderResponseModel>>


    /**
     * create Order
     */
    @POST("/api/v1/orders/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun createOrder(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Body request: OrderCreateRequestModel
    ): Observable<FreeResponseModel>


    /**
     * update Order State
     */
    @PUT("/api/v1/orders/{order_suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun updateOrderStatus(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String,
        @Body request: OrderUpdateRequestModel
    ): Observable<FreeResponseModel>


    /**
     * get Order Detail
     */
    @GET("/api/v1/orders/{order_suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getOrderDetail(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String
    ): Observable<OrderResponseModel>


    /**
     * Order Rate
     */
    @POST("/api/v1/orders/{order_suid}/rate")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun orderRate(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String,
        @Body request: ProductRateRequestModel
    ): Observable<FreeResponseModel>


    /**
     * Get SmartID Verification Code
     */
    @GET("/api/v1/orders/{order_suid}/smart-id/code/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getSmartIDVerificationCode(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String
    ): Observable<UserSmartIdVerificationResponseModel>

    /**
     * Start SmartID Auth
     */
    @POST("/api/v1/orders/{order_suid}/smart-id/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun startSmartIDAuth(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String
    ): Observable<FreeResponseModel>

    /**
     * Check smart-id session
     */
    @GET("/api/v1/orders/{order_suid}/smart-id/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun checkSmartIDSession(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String
    ): Observable<FreeResponseModel>



    /**
     * Start SmartID Auth
     */
    @POST("/api/v1/orders/{order_suid}/smart-id/cert/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun startSmartIDCert(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String
    ): Observable<FreeResponseModel>

    /**
     * Check smart-id session
     */
    @GET("/api/v1/orders/{order_suid}/smart-id/cert/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun checkSmartIDSessionCert(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String
    ): Observable<FreeResponseModel>


    /**
     * Setup stripe initial payment
     */
    @POST("/api/v1/orders/{order_suid}/stripe/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun initialPayment(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String
    ): Observable<InitialPaymentResponseModel>




    /**
     * Recheck payment auth
     */
    @PATCH("/api/v1/orders/{order_suid}/stripe/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun recheckPaymentAuth(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("order_suid") suid: String,
        @Body request: CheckPaymentRequestModel
    ): Observable<FreeResponseModel>




}