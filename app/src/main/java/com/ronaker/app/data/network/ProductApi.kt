package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.ProductCreateRequestModel
import com.ronaker.app.data.network.response.ProductCreateResponseModel
import com.ronaker.app.data.network.response.ProductDetailResponceModel
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


    /**
     * Register new user
     */
    @POST("/api/v1/products/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productCreate(@Header("Authorization") authToken: String?,@Body request: ProductCreateRequestModel): Observable<ProductCreateResponseModel>


    /**
     * Register new user
     */
    @GET("/api/v1/products/{suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getProduct(@Header("Authorization") authToken: String?,@Path("suid") suid:String): Observable<ProductDetailResponceModel>


}