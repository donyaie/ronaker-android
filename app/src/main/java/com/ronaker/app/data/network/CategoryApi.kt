package com.ronaker.app.data.network

import com.ronaker.app.data.network.response.CategoriesResponseModel
import com.ronaker.app.data.network.response.ListResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

/**
 * The interface which provides methods to get result of webservices
 */
interface CategoryApi {
    /**
     * get categories
     */
    @GET("/api/v1/categories/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getCategories(@Header("Authorization") authToken: String): Observable<ListResponseModel<CategoriesResponseModel>>
}