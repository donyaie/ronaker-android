package com.ronaker.app.data.network

import com.ronaker.app.data.network.response.ProductSearchResponceModel
import io.reactivex.Observable
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface ProductApi {
    /**
     * Register new user
     */
    @POST("/api/v1/products/search")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productSearch(@Header("Authorization") authToken: String?, @Query("page") page: Int): Observable<ProductSearchResponceModel>

}