package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.*
import com.ronaker.app.data.network.response.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface PaymentInfoApi {
    /**
     * get Payment info List
     */
    @GET("/api/v1/payment_info/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getPaymentInfoList(@Header("Authorization") authToken: String): Observable<ListResponseModel<PaymentInfoListResponseModel>>

    /**
     * get Payment info List
     */
    @POST("/api/v1/payment_info/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun addPaymentInfo(@Header("Authorization") authToken: String, @Body request: PaymentInfoCreateRequestModel): Observable<FreeResponseModel>



    /**
     * get Payment info List
     */
    @GET("/api/v1/financial_transactions/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getFinancialTransactions(@Header("Authorization") authToken: String): Observable<ListResponseModel<FinancialTransactionsResponseModel>>




}