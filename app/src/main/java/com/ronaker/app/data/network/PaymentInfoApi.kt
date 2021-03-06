package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.PaymentInfoCreateRequestModel
import com.ronaker.app.data.network.response.FinancialTransactionsResponseModel
import com.ronaker.app.data.network.response.FreeResponseModel
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.data.network.response.PaymentInfoListResponseModel
import io.reactivex.Observable
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface PaymentInfoApi {
    /**
     * get Payment Info List
     */
    @GET("/api/v1/payment_info/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getPaymentInfoList(@Header("Authorization") authToken: String,@Header("Accept-Language") language: String): Observable<ListResponseModel<PaymentInfoListResponseModel>>

    /**
     * get Payment Info List
     */
    @POST("/api/v1/payment_info/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun addPaymentInfo(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Body request: PaymentInfoCreateRequestModel
    ): Observable<FreeResponseModel>


    /**
     * get Payment Info List
     */
    @GET("/api/v1/financial_transactions/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getFinancialTransactions(@Header("Authorization") authToken: String,@Header("Accept-Language") language: String): Observable<ListResponseModel<FinancialTransactionsResponseModel>>


}