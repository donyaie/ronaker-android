package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.UserActivePhoneRequestModel
import com.ronaker.app.data.network.request.UserAddPhoneRequestModel
import com.ronaker.app.data.network.request.UserLoginRequestModel
import com.ronaker.app.data.network.request.UserRegisterRequestModel
import com.ronaker.app.data.network.response.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface CategoryApi {
    /**
     * get categories
     */
    @POST("/api/v1/categories/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getCategories(@Header("Authorization") authToken: String): Observable<ListResponseModel<CategoriesResponseModel>>
}